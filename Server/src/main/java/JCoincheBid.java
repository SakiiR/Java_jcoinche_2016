import java.util.ArrayList;

/**
 * Created by anakin on 20/11/16.
 */

/**
 * This Class is used to handle the game bid.
 * It will define the BidInformation needed
 * for one round.
 * It implements the method runBid() which
 * will generate the BidInformation.
 *
 * @see GameThread
 */
public class                            JCoincheBid {
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   allPlayers = null;
    private JCoinchePlayer              beginner = null;
    private JCoinchePlayer              bidBeginner = null;
    private CardGenerator               cardGenerator = null;
    private JCoincheBidInformations     bidInformations = null;
    private int                         bidValues[] = {80, 90, 100, 110, 120, 130, 140, 150, 160, 170};
    private GameThread                  gameThread = null;

    /**
     * JCoincheBid Constructor
     *
     * @param gameThread the gameThread from GameThread
     * @see GameThread
     */
    public                                  JCoincheBid(GameThread gameThread) {
        this.gameThread = gameThread;
        this.teams = this.gameThread.getTeams();
        this.allPlayers = this.gameThread.getAllPlayers();
        this.beginner = this.gameThread.getGeneralBeginner();
        this.cardGenerator = this.gameThread.getCardGenerator();

    }

    /**
     * Set the Beginner to a new JCoinche Player
     *
     * @param beginner the new beginner player
     * @return an instance of JCoincheBid
     */
    public JCoincheBid                      setBeginner(JCoinchePlayer beginner) {
        this.beginner = beginner;
        return this;
    }

    /**
     * Suggest a Bid to a player and set the
     * bidInformations if the bid is taken by the player .
     * Send a GET_BID Google Protocol Buffer message to the client.
     * Wait and receive a SET_BID Google Protocol Buffer message
     * from the client.
     * If the bif is taken, broadcast the bid to all the players
     * with a SEND_BID Google Protocol Buffer message.
     *
     * @param player is the client who receive a GET_BID message
     * and send a SET_BID message.
     * @return true if the client take the bid, false if he pass.
     */
    private boolean                         suggestBid(JCoinchePlayer player) {
        boolean                             valideMessage = false;
        JCoincheProtocol.JCoincheMessage    message = null;
        int                                 bidValue = (this.bidInformations.getBidValue() == 0 ? 80 : this.bidInformations.getBidValue() + 10);

        JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetBidMessage(bidValue));
        while (!valideMessage && this.gameThread.isRunning()) {
            message = player.getMessage();
            if (message != null) {
                JCoincheUtils.log("[>] Received message of type = %s, bidvalue = %d, bidtrump = %d", message.getType(), message.getSetBidMessage().getBidValue(), message.getSetBidMessage().getTrump());
                player.setMessage(null);
                if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_BID) {
                    JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeError("Wrong bid"));
                    JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetBidMessage(bidValue));
                } else {
                    if (message.getSetBidMessage().getBid()) {
                        if (!checkSetBidMessageValues(message, bidValue)) {
                            JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeError("Wrong bid"));
                            JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetBidMessage(bidValue));
                        } else {
                            valideMessage = true;
                        }
                    } else {
                        this.sendBidToAllPlayers(false, player);
                        return false;
                    }
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!this.gameThread.isRunning())
            return false;
        this.bidInformations.setBiddenPlayer(player).setBidValue(message.getSetBidMessage().getBidValue());
        this.bidInformations.setBidTrump(JCoincheBidInformations.BidTrump.valueOf(JCoincheBidInformations.BidTrump.values()[message.getSetBidMessage().getTrump()].name()));
        this.sendBidToAllPlayers(true, player);
        return true;
    }

    /**
     * This method send the information of the
     * bidInformations to all players.
     * It will specify the kind of bid : pass or
     * taken bid and call
     * the forgeSendBidMessage() method from the
     * MessageForger Class.
     *
     * @param bid a boolean false if the player pass, true if bid has been taken.
     * @param player the JCoinchePlayer who send the bid.
     * @see JCoinchePlayer
     * @see MessageForger
     * @see JCoincheBidInformations
     */
    private void                        sendBidToAllPlayers(boolean bid, JCoinchePlayer player) {
        for(JCoinchePlayer p : allPlayers) {
            if (!bid) {
                JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendBidMessage(this.bidInformations, false, player));
            } else {
                JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendBidMessage(this.bidInformations, true, player));
            }
        }
    }

    /**
     * Check if the SET_BID Google Protocol Buffer message
     * send by the player is complete and respect the actual game.
     *
     * @param message the SET_BID Google Protocol Buffer message
     * @param minBidValue the minimum bid value expected
     * @return true is the message is good, false if it's wrong
     */
    private boolean                     checkSetBidMessageValues(JCoincheProtocol.JCoincheMessage message, int minBidValue) {
        int                             resBidValue;
        int                             resBidTrump;
        boolean                         goodValue = false;

        if (!message.getSetBidMessage().hasBidValue() || !message.getSetBidMessage().hasTrump()) {
            return false;
        }
        resBidValue = message.getSetBidMessage().getBidValue();
        if (resBidValue < minBidValue) {
            return false;
        }
        for(int i : this.bidValues) {
            if (i == resBidValue) {
                goodValue = true;
            }
        }
        if (!goodValue) {
            return false;
        }
        resBidTrump = message.getSetBidMessage().getTrump();
        return (resBidTrump >= 0 && resBidTrump <= 5);
    }

    /**
     * The run method for the bid.
     * Running until a bid is taken and set.
     * Ask for the coinche and the surcoinche.
     *
     */
    public void                         runBid()
    {
        boolean                         hasBid = false;
        int                             pass = 0;

        this.bidInformations = new JCoincheBidInformations();
        while (!hasBid && this.gameThread.isRunning()) {
            this.cardGenerator.spreadCards(allPlayers);
            this.sendCardsToAllPlayers();
            this.bidBeginner = this.beginner;
            pass = 0;
            while (pass < 4 && !hasBid && this.gameThread.isRunning()) {
                if (!(this.suggestBid(this.bidBeginner))) {
                    pass++;
                    if (this.bidBeginner.getId() == 4) {
                        this.bidBeginner = this.allPlayers.get(0);
                    } else {
                        this.bidBeginner = this.allPlayers.get(this.bidBeginner.getId());
                    }
                } else {
                    if (this.bidInformations.getBidValue() == 170) {
                        hasBid = true;
                    }
                    else {
                        hasBid = this.resuggerBid(this.bidBeginner);
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
        if (!this.gameThread.isRunning()) {
            return;
        }
        if (this.suggestCoinche()) {
            this.suggestSurCoinche();
        }
    }

    /**
     * Suggest the Coinche to the adverse team of the
     * bidder team.
     * Send a GET_COINCHE Google Protocol Buffer Message
     * to the adverse team.
     * Wait for a SET_COINCHE Google Protocol Buffer Message.
     *
     * @return true if the coinche is taken, false if it's not.
     */
    private boolean                         suggestCoinche() {
        int                                 teamNumber;
        boolean                             valideMessage;
        JCoincheProtocol.JCoincheMessage    message = null;

        JCoincheUtils.log("biddenPlayer id = %d", this.bidInformations.getBiddenPlayer().getId());
        if (this.bidInformations.getBiddenPlayer().getId() == 1 || this.bidInformations.getBiddenPlayer().getId() == 3)
            teamNumber = 1;
        else
            teamNumber = 0;
        //send message to adversaire
        for (JCoinchePlayer p : this.teams.get(teamNumber).getPlayers()) {
            JCoincheUtils.log("[>] Sending message to player id = %d", p.getId());
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGetCoincheMessage());
            valideMessage = false;
            while (!valideMessage && this.gameThread.isRunning()) {
                message = p.getMessage();
                if (message != null) {
                    p.setMessage(null);
                    if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_COINCHE) {
                        JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeError("Wrong Coinche"));
                        JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGetCoincheMessage());
                    } else {
                         valideMessage = true;
                        if (message.getSetCoincheMessage().getCoinche()) {
                            this.bidInformations.setCoinche(true);
                            JCoincheUtils.log("[>] Coinche made ! Sending Coinche to all players");
                            //broadcast a tout les players s'il y a coinche
                            for (JCoinchePlayer player : this.allPlayers) {
                                JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeSendCoincheMessage(p.getId()));
                            }
                            return true;
                        }
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Suggest Surcoinche to the bidder team if
     * the coinche has been taken by the adverse team.
     * Send a GET_SURCOINCHE Google Protocol Buffer Message
     * to the bidder team.
     * Wait for a SET_SURCOINCHE Google Protocol Buffer Message.
     */
    private void                            suggestSurCoinche() {
        boolean                             validemessage = false;
        JCoincheProtocol.JCoincheMessage    message = null;
        JCoinchePlayer                      bidder = null;

        bidder = this.bidInformations.getBiddenPlayer();
        JCoincheUtils.writeAndFlush(bidder.getChannel(), MessageForger.forgeGetSurcoincheMessage());
        while (!validemessage && this.gameThread.isRunning()) {
            message = bidder.getMessage();
            if (message != null) {
                bidder.setMessage(null);
                if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_SURCOINCHE) {
                    JCoincheUtils.writeAndFlush(bidder.getChannel(), MessageForger.forgeError("Wrong Surcoinche"));
                    JCoincheUtils.writeAndFlush(bidder.getChannel(), MessageForger.forgeGetSurcoincheMessage());
                } else {
                    validemessage = true;
                    if (message.getSetSurcoincheMessage().getSurcoinche()) {
                        this.bidInformations.setSurcoinche(true);
                        for (JCoinchePlayer p : this.allPlayers) {
                            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendSurcoincheMessage(bidder.getId()));
                        }
                    }
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Getter for the bidInformations.
     * @return the bidInformations
     */
    public JCoincheBidInformations      getBidInformations() {
        return bidInformations;
    }

    /**
     * Resugger the bid if a player has taken the bid.
     * Run until 3 players pass the bid.
     *
     * @param bidder the bidder player
     * @return true if bid is taken, false if the game stop running
     */
    private boolean                     resuggerBid(JCoinchePlayer bidder) {

        int                             pass = 0;
        boolean                         takebid = false;
        JCoinchePlayer                  actualPlayerDemand;

        if (bidder.getId() == 4)
            actualPlayerDemand = allPlayers.get(0);
        else
            actualPlayerDemand = allPlayers.get(bidder.getId());
        while (pass < 3 && this.gameThread.isRunning()) {
            if (!(takebid = suggestBid(actualPlayerDemand))) {
                if (actualPlayerDemand.getId() == 4)
                    actualPlayerDemand = allPlayers.get(0);
                else
                    actualPlayerDemand = allPlayers.get(actualPlayerDemand.getId());
                pass++;
            }
            else
                break;
        }
        if (pass == 3 || this.bidInformations.getBidValue() == 170) {
            return true;

        }
        if (!this.gameThread.isRunning()) {
            return false;
        }
        return (this.resuggerBid(actualPlayerDemand));
    }

    /**
     * Send cards to all players.
     */
    private void                        sendCardsToAllPlayers() {
        for (JCoinchePlayer p : this.allPlayers) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGetCardsMessage(p));
        }
    }
}
