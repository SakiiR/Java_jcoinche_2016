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
        //chercher carte gagnante
        // attribuer point au trick score
        // set le trick beginner au dernier gagnant

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
