import java.util.ArrayList;

/**
 * Created by anakin on 24/11/16.
 */
public class                    JCoincheTrick {
    JCoinchePlayer              trickBeginner = null;
    JCoinchePlayer              actualPlayer = null;
    ArrayList<JCoincheCard>     cards = null;
    ArrayList<JCoincheTeam>     teams = null;
    ArrayList<JCoinchePlayer>   players = null;
    JCoincheBidInformations     bidInformations = null;

    public                      JCoincheTrick(JCoinchePlayer beginner, ArrayList<JCoincheTeam> teams,
                                              ArrayList<JCoinchePlayer> players, JCoincheBidInformations bidInformations) {
        this.trickBeginner = beginner;
        this.teams = teams;
        this.players = players;
       /* if (this.trickBeginner.getId() == 4)
            this.actualPlayer = this.players.get(0);
        else
            this.actualPlayer = this.players.get(this.trickBeginner.getId());*/
       this.actualPlayer = this.trickBeginner;
        this.bidInformations = bidInformations;
        this.cards = new ArrayList<>();
    }

    public JCoinchePlayer       getTrickBeginner() {
        return trickBeginner;
    }


    public void                  run() {

        while(this.cards.size() < 4 && GameThread.isRunning) {
            if (this.cards.size() == 0)
                this.getCardFromBeginner();
            else
                this.getCardFromPlayer();
            if (this.actualPlayer.getId() == 4)
                this.actualPlayer = this.players.get(0);
            else
                this.actualPlayer = this.players.get(this.actualPlayer.getId());
        }
        if (!GameThread.isRunning) return;
        this.evaluateCards(this.bidInformations, this.cards);
    }

    private boolean                         evaluateCards(JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        JCoinchePlayer                      winner = null;
        int                                 score = 0;

        JCoincheUtils.logInfo("before generate value cards");
        this.generateValueCards(bidInfo, cardsOnTable);
        JCoincheUtils.logInfo("after generate value cards");
        if (bidInfo.getBidTrump().ordinal() < 4) /*si jeu normal on envoi le calcul normal */
            winner = this.generateWinnerTrickNormalGame(bidInfo, cardsOnTable);
        else if (bidInfo.getBidTrump().ordinal() >= 4) /*si jeu sans atout ou tout atout, on envoi le calcul sans atout */
            winner = this.generateWinnerTrick(bidInfo, cardsOnTable);
        if (winner == null) {
            JCoincheUtils.logInfo("winner is null");
            return false;
        }
        for (JCoincheCard c : cardsOnTable) {
            score += c.getValue();
        }
        winner.getTeam().setTrickScore(winner.getTeam().getTrickScore() + score);
        this.trickBeginner = winner;
        JCoincheUtils.logInfo("winnerid = %d teamid = %d trickscore = %d ", winner.getId(), winner.getTeam().getId(), winner.getTeam().getTrickScore());
        for (JCoinchePlayer p : this.players)
        {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendWinTrickMessage(winner.getId(), winner.getTeam().getId(), score));
        }
        return true;
    }


    private JCoinchePlayer                  generateWinnerTrickNormalGame(JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        ArrayList<JCoincheCard>             cardsEval = new ArrayList<>();
        JCoinchePlayer                      winner = null;
        int                                 tmpValue;

        JCoincheUtils.logInfo("inside generateWinnerTrickNormalGame");
        cardsEval = this.findTrumps(cardsEval, bidInfo, cardsOnTable); /* on cherche les atouts sur la table */
        if (cardsEval.size() == 1) { /*si 1 atout trouvé on set le winner */
            JCoincheUtils.logInfo("found one trump");
            winner = cardsEval.get(0).getPlayer();
        } else if (cardsEval.size() > 1) { /*si plusieurs atout trouvé on prend le plus grand */
            tmpValue = 0;
            JCoincheUtils.logInfo("find multiple trumps");
            for (JCoincheCard c : cardsEval) {
                if (c.getValue() >= tmpValue) {
                    tmpValue = c.getValue();
                    winner = c.getPlayer();
                }
            }
        } else if (cardsEval.size() == 0) {/*si pas d'atout trouvé on envoi l'eval sans atout dans le jeu*/
            JCoincheUtils.logInfo("trump not find, go inside generateWinnertrick");
            winner = this.generateWinnerTrick(bidInfo, cardsOnTable);
        }
        return winner;
    }

    private JCoinchePlayer                  generateWinnerTrick(JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        ArrayList<JCoincheCard>             cardsEval = new ArrayList<>();
        JCoinchePlayer                      winner = null;
        int                                 tmpValue = 0;

        cardsEval = this.findCardsColor(cardsEval, bidInfo, cardsOnTable); /* on cherche les cartes de la couleur de la première carte*/
        if (cardsEval.size() == 1) { /* si une seule carte => la premiere posée, on set le winner */
            winner = cardsOnTable.get(0).getPlayer();
        } else if (cardsEval.size() > 1) { /* si plusieurs carte de la meme couleur que la premiere on cherche la plus forte et on set le winner*/
            for (JCoincheCard c : cardsEval) {
                if (c.getValue() >= tmpValue) {
                    tmpValue = c.getValue();
                    winner = c.getPlayer();
                }
            }
        }
        return winner;
    }

    private ArrayList<JCoincheCard>         findTrumps(ArrayList<JCoincheCard> cardsEval, JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        for (JCoincheCard c : cardsOnTable) {
            if (c.getColor().ordinal() == bidInfo.getBidTrump().ordinal()) {
                cardsEval.add(c);
            }
        }
        return cardsEval;
    }

    private ArrayList<JCoincheCard>         findCardsColor(ArrayList<JCoincheCard> cardsEval, JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        for (JCoincheCard c : cardsOnTable) {
            if (c.getColor().ordinal() == cardsOnTable.get(0).getColor().ordinal()) {
                cardsEval.add(c);
            }
        }
        return cardsEval;
    }

    private void                            generateValueCards(JCoincheBidInformations bidInfo, ArrayList<JCoincheCard> cardsOnTable) {
        for (JCoincheCard c : cardsOnTable) {
            if (bidInfo.getBidTrump().ordinal() < 4) { /*si jeu normal, si atout prend valeur atout, sinon valeur sans atout*/
                if (c.getColor().ordinal() == bidInfo.getBidTrump().ordinal()) {
                    c.setValue(c.cardValueTrump[c.getId().ordinal()]);
                } else {
                    c.setValue(c.cardValue[c.getId().ordinal()]);
                }
            } else if (bidInfo.getBidTrump().ordinal() == 4) { /*si jeu sans atout prend valeur sans atout*/
                c.setValue(c.cardValue[c.getId().ordinal()]);
            } else { /*si jeu tout atout, prend valeur tout atout*/
                c.setValue(c.cardValueTrump[c.getId().ordinal()]);
            }
        }
    }

    private void                            sendGetCardError(JCoinchePlayer player) {
        JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeError("Wrong card"));
        JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetCardMessage());
    }

    private void                            getCardFromPlayer() {
        boolean                             valideMessage = false;
        JCoincheProtocol.JCoincheMessage    message = null;

        JCoincheUtils.writeAndFlush(this.actualPlayer.getChannel(), MessageForger.forgeGetCardMessage());
        while(!valideMessage && GameThread.isRunning) {
            message = this.actualPlayer.getMessage();
            if (message != null) {
                this.actualPlayer.setMessage(null);
                if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_CARD) {
                    this.sendGetCardError(this.actualPlayer);
                } else {
                    if (!this.checkCardOfPlayer(message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor(), this.actualPlayer)) {
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
        if (!GameThread.isRunning) return;
        this.addCardtoCards(message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor(), this.actualPlayer);
        this.sendCardtoPlayers(this.actualPlayer, message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor());
    }

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

    private void                            getCardFromBeginner() {
        boolean                             valideMessage = false;
        JCoincheProtocol.JCoincheMessage    message = null;

        JCoincheUtils.writeAndFlush(this.trickBeginner.getChannel(), MessageForger.forgeGetCardMessage());
        while (!valideMessage && GameThread.isRunning) {
            message = this.trickBeginner.getMessage();
            if (message != null) {
                this.trickBeginner.setMessage(null);
                if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_CARD) {
                    this.sendGetCardError(this.trickBeginner);
                    //on check si la carte est présente dans le jeu;
                } else {
                    if (!this.checkCardOfPlayer(message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor(), this.trickBeginner)) {
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
        if (!GameThread.isRunning) return;
        //on add la card au tapis et on la retire du jeu du player
        this.addCardtoCards(message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor(), this.trickBeginner);
        this.sendCardtoPlayers(this.trickBeginner, message.getSetCardMessage().getCardId(), message.getSetCardMessage().getCardColor());
    }

    private boolean                           checkCardOfPlayer(int cardId, int cardColor, JCoinchePlayer player) {
        for (JCoincheCard c : player.getCards()) {
            if (c.getId().ordinal() == cardId && c.getColor().ordinal() == cardColor) {
                return true;
            }
        }
        return false;
    }

    private void                               addCardtoCards(int cardId, int cardColor, JCoinchePlayer player) {
        for (JCoincheCard c : player.getCards()) {
            if (c.getId().ordinal() == cardId && c.getColor().ordinal() == cardColor) {
                this.cards.add(c);
                player.getCards().remove(c);
                return;
            }
        }
    }

    private void                                sendCardtoPlayers(JCoinchePlayer player, int cardId, int cardColor) {
        for (JCoinchePlayer p : this.players) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendCardMessage(player.getId(), cardId, cardColor));
        }
    }
}
