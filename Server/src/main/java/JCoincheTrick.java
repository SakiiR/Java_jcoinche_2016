import java.util.ArrayList;

/**
 * Created by anakin on 24/11/16.
 */

/**
 * This Class defined a unique trick
 * composed of 4 getting cards.
 * It will evaluate the winner of the trick
 * and generate a temporaly trickscore for
 * each team.
 */
public class                            JCoincheTrick {
    private JCoinchePlayer              trickBeginner = null;
    private JCoinchePlayer              actualPlayer = null;
    private ArrayList<JCoincheCard>     cards = null;
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   players = null;
    private JCoincheBidInformations     bidInformations = null;
    private GameThread                  gameThread = null;

    /**
     * The constructor of a JCoincheTrick
     *
     * @param beginner the new trick beginner
     * @param teams the teams
     * @param players the players
     * @param bidInformations the bidInformations
     * @param gameThread the GameThread from GameThread
     */
    public                              JCoincheTrick(JCoinchePlayer beginner, ArrayList<JCoincheTeam> teams,
                                              ArrayList<JCoinchePlayer> players, JCoincheBidInformations bidInformations,
                                              GameThread gameThread) {
        this.trickBeginner = beginner;
        this.teams = teams;
        this.players = players;
        this.gameThread = gameThread;
        this.actualPlayer = this.trickBeginner;
        this.bidInformations = bidInformations;
        this.cards = new ArrayList<>();
    }

    /**
     * A getter for the trickBeginner.
     *
     * @return the trickBeginner
     */
    public JCoinchePlayer       getTrickBeginner() {
        return trickBeginner;
    }


    /**
     * This method run until 4 cards have been set.
     * It will send a GET_CARD Google Protocol Buffer
     * message to a player and wait for a SET_CARD Google Protocol
     * Buffer from the player.
     * It will call the evaluateCards method to set the
     * trickScore and the trick winner.
     */
    public void                  run() {

        while(this.cards.size() < 4 && this.gameThread.isRunning()) {
            if (this.cards.size() == 0)
                this.getCardFromBeginner();
            else
                this.getCardFromPlayer();
            if (this.actualPlayer.getId() == 4)
                this.actualPlayer = this.players.get(0);
            else
                this.actualPlayer = this.players.get(this.actualPlayer.getId());
        }
        if (!this.gameThread.isRunning()) return;
        this.evaluateCards(this.bidInformations, this.cards);
    }

    /**
     * This method will evaluate the cards.
     * It will call the generateValueCards then
     * set the winner trick and the score with the
     * bidInformations.
     * It will broadcast a SEND_WIN_TRICK Google Protocol
     * Buffer message to all players.
     *
     * @param bidInfo the bidInformations
     * @param cardsOnTable an array of 4 cards
     * @return a boolean false if an error occur or a true
     * if everything is ok
     */
    private boolean                         evaluateCards(JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        JCoinchePlayer                      winner = null;
        int                                 score = 0;

        this.generateValueCards(bidInfo, cardsOnTable);
        if (bidInfo.getBidTrump().ordinal() < 4)
            winner = this.generateWinnerTrickNormalGame(bidInfo, cardsOnTable);
        else if (bidInfo.getBidTrump().ordinal() >= 4)
            winner = this.generateWinnerTrick(bidInfo, cardsOnTable);
        if (winner == null) {
            JCoincheUtils.logInfo("[!] winner is null");
            return false;
        }
        for (JCoincheCard c : cardsOnTable) {
            score += c.getValue();
        }
        winner.getTeam().setTrickScore(winner.getTeam().getTrickScore() + score);
        this.trickBeginner = winner;
        JCoincheUtils.logInfo("[>] winnerid = %d teamid = %d trickscore = %d ", winner.getId(),
                winner.getTeam().getId(), winner.getTeam().getTrickScore());
        for (JCoinchePlayer p : this.players)
        {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendWinTrickMessage(winner.getId(),
                    winner.getTeam().getId(), score));
        }
        return true;
    }

    /**
     * This method will generate the winner
     * for a normal bid with a trump and a value.
     *
     * @param bidInfo the bidInformations
     * @param cardsOnTable an array of 4 cards
     * @return the winner of the trick
     */
    private JCoinchePlayer                  generateWinnerTrickNormalGame(JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        ArrayList<JCoincheCard>             cardsEval = new ArrayList<>();
        JCoinchePlayer                      winner = null;
        int                                 tmpValue;

        JCoincheUtils.logInfo("[>] inside generateWinnerTrickNormalGame");
        cardsEval = this.findTrumps(cardsEval, bidInfo, cardsOnTable);
        if (cardsEval.size() == 1) {
            JCoincheUtils.logInfo("[>] found one trump");
            winner = cardsEval.get(0).getPlayer();
        } else if (cardsEval.size() > 1) {
            tmpValue = -1;
            JCoincheUtils.logInfo("[>] found multiple trumps");
            for (JCoincheCard c : cardsEval) {
                if (c.getValue() > tmpValue) {
                    tmpValue = c.getValue();
                    winner = c.getPlayer();
                }
            }
        } else if (cardsEval.size() == 0) {
            JCoincheUtils.logInfo("[>] trump not find, go inside generateWinnertrick");
            winner = this.generateWinnerTrick(bidInfo, cardsOnTable);
        }
        return winner;
    }

    /**
     * This method will generate a winner trick
     * if there is a WT or an AT bid.
     *
     * @param bidInfo the bidInformations
     * @param cardsOnTable an array of 4 cards
     * @return the winner trick
     */
    private JCoinchePlayer                  generateWinnerTrick(JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        ArrayList<JCoincheCard>             cardsEval = new ArrayList<>();
        JCoinchePlayer                      winner = null;
        int                                 tmpValue = -1;

        cardsEval = this.findCardsColor(cardsEval, bidInfo, cardsOnTable);
        if (cardsEval.size() == 1) {
            winner = cardsOnTable.get(0).getPlayer();
        } else if (cardsEval.size() > 1) {
            for (JCoincheCard c : cardsEval) {
                if (c.getValue() > tmpValue) {
                    tmpValue = c.getValue();
                    winner = c.getPlayer();
                }
            }
        }
        return winner;
    }

    /**
     * This method will find trumps in an array of cards
     * depending of the bidTrump from the bidInformations.
     * It will add the card trump to a cardsEval that is
     * returned.
     *
     * @param cardsEval an array of cardsEval
     * @param bidInfo the bidInformations
     * @param cardsOnTable an array of 4 cards
     * @return the cardsEval with cardTrump founded
     */
    private ArrayList<JCoincheCard>         findTrumps(ArrayList<JCoincheCard> cardsEval, JCoincheBidInformations bidInfo,
                                                       ArrayList<JCoincheCard> cardsOnTable) {
        for (JCoincheCard c : cardsOnTable) {
            if (c.getColor().ordinal() == bidInfo.getBidTrump().ordinal()) {
                cardsEval.add(c);
            }
        }
        return cardsEval;
    }

    /**
     * This method will find color cards in an array of
     * cards depending of the color of the first card in
     * the array.
     * It will add the color card to a cardsEval that is
     * returned.
     *
     * @param cardsEval an array of cardsEval
     * @param bidInfo the bidInformations
     * @param cardsOnTable an array of 4 cards
     * @return the cardsEval with cardColor founded
     */
    private ArrayList<JCoincheCard>         findCardsColor(ArrayList<JCoincheCard> cardsEval, JCoincheBidInformations bidInfo,
                                                           ArrayList<JCoincheCard> cardsOnTable) {
        for (JCoincheCard c : cardsOnTable) {
            if (c.getColor().ordinal() == cardsOnTable.get(0).getColor().ordinal()) {
                cardsEval.add(c);
            }
        }
        return cardsEval;
    }

    /**
     * This method will generate the value of
     * each card depending of the bidInformations
     *
     * @param bidInfo the bidInformations
     * @param cardsOnTable an array of 4 cards
     */
    private void                            generateValueCards(JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        for (JCoincheCard c : cardsOnTable) {
            if (bidInfo.getBidTrump().ordinal() < 4) {
                if (c.getColor().ordinal() == bidInfo.getBidTrump().ordinal()) {
                    c.setValue(c.cardValueTrump[c.getId().ordinal()]);
                } else {
                    c.setValue(c.cardValue[c.getId().ordinal()]);
                }
            } else if (bidInfo.getBidTrump().ordinal() == 4) {
                c.setValue(c.cardValue[c.getId().ordinal()]);
            } else {
                c.setValue(c.cardValueTrump[c.getId().ordinal()]);
            }
        }
    }

    /**
     * Send an ERROR Google Protocol Buffer message
     * for a wring card.
     *
     * @param player a JCoinchePlayer to send the error
     */
    private void                            sendGetCardError(JCoinchePlayer player) {
        JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeError("Wrong card"));
        JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetCardMessage());
    }

    /**
     * This method will send a GET_CARD Google Protocol
     * Buffer message to a player.
     * It will run until a SET_CARD Google Protocol Buffer
     * message is send from the player to the server.
     * It will add the card to the cardsOnTable and broadcast
     * this action to all players.
     */
    private void                            getCardFromPlayer() {
        boolean                             valideMessage = false;
        JCoincheProtocol.JCoincheMessage    message = null;

        JCoincheUtils.writeAndFlush(this.actualPlayer.getChannel(), MessageForger.forgeGetCardMessage());
        while(!valideMessage && this.gameThread.isRunning()) {
            message = this.actualPlayer.getMessage();
            if (message != null) {
                this.actualPlayer.setMessage(null);
                if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_CARD) {
                    this.sendGetCardError(this.actualPlayer);
                } else {
                    if (!this.checkCardOfPlayer(message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor(),
                            this.actualPlayer)) {
                        this.sendGetCardError(this.actualPlayer);
                    } else {
                        if (!this.checkCardValidity(message.getSetCardMessage().getCardColor(), this.actualPlayer)) {
                            this.sendGetCardError(this.actualPlayer);
                        } else {
                            valideMessage = true;
                        }
                    }
                }
            }
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!this.gameThread.isRunning()) return;
        this.addCardtoCards(message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor(), this.actualPlayer);
        this.sendCardtoPlayers(this.actualPlayer, message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor());
    }

    /**
     * This method will check if the setting card
     * is valid depending of the game
     *
     * @param colorId the colorId of the card
     * @param player the JCoinchePlayer who plays this card
     * @return false if isn't valid, true if is
     * @see JCoincheCard
     */
    private boolean                         checkCardValidity(int colorId, JCoinchePlayer player) {
        if (colorId == this.cards.get(0).getColor().ordinal())
            return true;
        else {
            for (JCoincheCard c : player.getCards())
            {
                if (c.getColor().ordinal() == this.cards.get(0).getColor().ordinal())
                    return false;
            }
        }
        return true;
    }

    /**
     * This method will send a GET_CARD Google Protocol
     * Buffer message to the first player.
     * It will run until a SET_CARD Google Protocol Buffer
     * message is send from the player to the server.
     * It will broadcast the card to all player.
     */
    private void                            getCardFromBeginner() {
        boolean                             valideMessage = false;
        JCoincheProtocol.JCoincheMessage    message = null;

        JCoincheUtils.writeAndFlush(this.trickBeginner.getChannel(), MessageForger.forgeGetCardMessage());
        while (!valideMessage && this.gameThread.isRunning()) {
            message = this.trickBeginner.getMessage();
            if (message != null) {
                this.trickBeginner.setMessage(null);
                if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_CARD) {
                    this.sendGetCardError(this.trickBeginner);
                } else {
                    if (!this.checkCardOfPlayer(message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor(),
                            this.trickBeginner)) {
                        this.sendGetCardError(this.trickBeginner);
                    } else {
                        valideMessage = true;
                    }
                }
            }
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!this.gameThread.isRunning()) return;
        this.addCardtoCards(message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor(), this.trickBeginner);
        this.sendCardtoPlayers(this.trickBeginner, message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor());
    }

    /**
     * This method will check if a card is present
     * in the array of cards of a specific player.
     *
     * @param cardId the Id of the card
     * @param cardColor the color of the card
     * @param player the player to analyse
     * @return true if the card exist, false if is not
     * @see JCoinchePlayer
     * @see JCoincheCard
     */
    private boolean                           checkCardOfPlayer(int cardId, int cardColor, JCoinchePlayer player) {
        for (JCoincheCard c : player.getCards()) {
            if (c.getId().ordinal() == cardId && c.getColor().ordinal() == cardColor) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will add a card to an array of cards
     * and remove it from a player.
     *
     * @param cardId the Id card
     * @param cardColor the color card
     * @param player the JCoinchePlayer
     * @see JCoincheCard
     * @see JCoinchePlayer
     */
    private void                               addCardtoCards(int cardId, int cardColor, JCoinchePlayer player) {
        for (JCoincheCard c : player.getCards()) {
            if (c.getId().ordinal() == cardId && c.getColor().ordinal() == cardColor) {
                this.cards.add(c);
                player.getCards().remove(c);
                return;
            }
        }
    }

    /**
     * This method will broadcast a SEND_CARD Google
     * Protocol Buffer message to all players.
     *
     * @param player the player who set the card
     * @param cardId the cardId
     * @param cardColor the cardColor
     * @see JCoincheCard
     * @see JCoinchePlayer
     */
    private void                                sendCardtoPlayers(JCoinchePlayer player, int cardId, int cardColor) {
        for (JCoinchePlayer p : this.players) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendCardMessage(player.getId(), cardId, cardColor));
        }
    }
}
