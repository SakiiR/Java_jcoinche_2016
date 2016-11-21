import java.util.ArrayList;

/**
 * Created by anakin on 20/11/16.
 */
public class                            JCoincheBid {
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   allPlayers = null;
    private JCoinchePlayer              beginner = null;
    private JCoinchePlayer              bidBeginner = null;
    private CardGenerator               cardGenerator = null;
    private JCoincheBidInformations     bidInformations = null;
    private int                         bidValues[] = {80, 90, 100, 110, 120, 130, 140, 150, 160, 170};


    public                              JCoincheBid(ArrayList<JCoincheTeam> teams,
                                                    ArrayList<JCoinchePlayer> players,
                                                    JCoinchePlayer beginner,
                                                    CardGenerator cardGenerator) {
        this.teams = teams;
        this.allPlayers = players;
        this.beginner = beginner;
        this.cardGenerator = cardGenerator;

    }

    public JCoincheBid                   setBeginner(JCoinchePlayer beginner) {
        this.beginner = beginner;
        return this;
    }

    private boolean                         suggestBid(JCoinchePlayer player) {
        boolean                             valideMessage = false;
        JCoincheProtocol.JCoincheMessage    message = null;
        int                                 bidValue = (this.bidInformations.getBidValue() == 0 ? 80 : this.bidInformations.getBidValue() + 10);

        JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetBidMessage(bidValue));
        while (!valideMessage && GameThread.isRunning) {
            message = player.getMessage();
            if (message != null) {
                JCoincheUtils.log("received message of type = %s, bidvalue = %d, bidtrump = %d", message.getType(), message.getSetBidMessage().getBidValue(), message.getSetBidMessage().getTrump());
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
                        this.bidInformations.setBiddenPlayer(player);
                        this.sendBidToAllPlayers(false);
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
        if (!GameThread.isRunning)
            return false;
        this.bidInformations.setBiddenPlayer(player).setBidValue(message.getSetBidMessage().getBidValue());
        if (message.getSetBidMessage().getTrump() < 4) {
            this.bidInformations.setBidTrump(JCoincheCard.Color.valueOf(JCoincheCard.Color.values()[message.getSetBidMessage().getTrump()].name()));
            this.bidInformations.setBidType(null);
        }
        else {
            this.bidInformations.setBidType(JCoincheBidInformations.BidType.valueOf(JCoincheBidInformations.BidType.values()[message.getSetBidMessage().getTrump() - 4].name()));
            this.bidInformations.setBidTrump(null);
        }
        this.sendBidToAllPlayers(true);
        return true;
    }

    private void                        sendBidToAllPlayers(boolean bid) {
        for(JCoinchePlayer p : allPlayers) {
            if (!bid) {
                JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendBidMessage(this.bidInformations, false));
            } else {
                JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendBidMessage(this.bidInformations, true));
            }
        }
    }

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

    public void                         runBid()
    {
        boolean                         hasBid = false;
        int                             pass = 0;

        this.bidInformations = new JCoincheBidInformations();
        while (!hasBid && GameThread.isRunning) {
            this.cardGenerator.spreadCards(allPlayers);
            this.sendCardsToAllPlayers();
            this.bidBeginner = this.beginner;
            pass = 0;
            while (pass < 4 && !hasBid && GameThread.isRunning) {
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
        if (!GameThread.isRunning) {
            return;
        }
        if (this.suggestCoinche()) {
            this.suggestSurCoinche();
        }
    }

    private boolean                         suggestCoinche() {
        int                                 teamNumber;
        boolean                             valideMessage = false;
        JCoincheProtocol.JCoincheMessage    message = null;

        if (this.bidInformations.getBiddenPlayer().getId() == 0 || this.bidInformations.getBiddenPlayer().getId() == 2)
            teamNumber = 1;
        else
            teamNumber = 0;
        //send message to adversaire
        for (JCoinchePlayer p : this.teams.get(teamNumber).getPlayers()) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGetCoincheMessage());
            while (!valideMessage && GameThread.isRunning) {
                message = p.getMessage();
                if (message != null) {
                    p.setMessage(null);
                    if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_COINCHE) {
                        JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeError("Wrong Coinche"));
                        JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGetCoincheMessage());
                    } else {
                        valideMessage = true;
                        if (message.getSetCoincheMessage().getCoincheValue()) {
                            this.bidInformations.setCoinche(true);
                            //broadcast tout les clients qu'il y a coinche
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

    private void                        suggestSurCoinche() {

    }

    private boolean                     resuggerBid(JCoinchePlayer bidder) {

        int                             pass = 0;
        boolean                         takebid = false;
        JCoinchePlayer                  actualPlayerDemand;

        if (bidder.getId() == 4)
            actualPlayerDemand = allPlayers.get(0);
        else
            actualPlayerDemand = allPlayers.get(bidder.getId());
        while (pass < 3 && GameThread.isRunning) {
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
        if (pass == 3) {
            return true;
        }
        if (!GameThread.isRunning) {
            return false;
        }
        return (this.resuggerBid(actualPlayerDemand));
    }

    private void                        sendCardsToAllPlayers() {
        for (JCoinchePlayer p : this.allPlayers) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGetCardsMessage(p));
        }
    }
}
