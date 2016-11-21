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
        int                                 resBidValue;
        int                                 bidValue = (this.bidInformations.getBidValue() == 0 ? 80 : this.bidInformations.getBidValue() + 10);

        JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetBidMessage(bidValue));
        while (!valideMessage && GameThread.isRunning) {
            if ((message = player.getMessage()) != null) {
                player.setMessage(null);
                if (message.getType() != JCoincheProtocol.JCoincheMessage.Type.SET_BID) {
                    JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeError("Wrong bid"));
                    JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetBidMessage(bidValue));
                } else {
                    if (message.getBid()) {
                        if (!checkSetBidMessageValues(message, bidValue)) {
                            JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeError("Wrong bid"));
                            JCoincheUtils.writeAndFlush(player.getChannel(), MessageForger.forgeGetBidMessage(bidValue));
                        }
                        else
                            valideMessage = true;
                    } else
                        return false;
                }
            }
        }
        if (!GameThread.isRunning)
            return false;
        this.bidInformations.setBiddenPlayer(player).setBidValue(message.getValue());
        if (message.getTrump() < 4) {
            this.bidInformations.setBidTrump(message.getTrump());
            this.bidInformations.setBidType(null);
        }
        else {
            this.bidInformations.setBidType(message.getTrump() - 4);
            this.bidInformations.setBidTrump(null);
        }

        //lecture du dernier message reçu => boucle tant que message invalide (set_bid + bonne value si enchère + envoi erreur
        //message reçu valide => si pass return false, sinon enchère fait => set bidInfo broadcast aux autres joueurs return true
        return true;
    }

    private boolean                     checkSetBidMessageValues(JCoincheProtocol.JCoincheMessage message, int minBidValue) {
        int                             resBidValue;
        int                             resBidTrump;
        boolean                         goodValue = false;

        if (!message.hasValue())
            return false;
        if (!message.hasTrump())
            return false;
        resBidValue = message.getValue();
        if (resBidValue < minBidValue)
            return false;
        for(int i : this.bidValues) {
            if (i == resBidValue)
                goodValue = true;
        }
        if (!goodValue)
            return false;
        resBidTrump = message.getTrump();
        if (resBidTrump < 0 || resBidTrump > 5)
            return false;
        return true;

    }

    public void                         runBid()
    {
        boolean                         hasBid = false;
        boolean                         takebid = false;
        int                             pass = 0;

        this.bidInformations = new JCoincheBidInformations();
        while (!hasBid && GameThread.isRunning) {
            this.cardGenerator.spreadCards(allPlayers);
            this.sendCardsToAllPlayers();
            this.bidBeginner = this.beginner;
            pass = 0;
            while (pass < 4 && !hasBid && GameThread.isRunning) {
                if (!(takebid = this.suggestBid(this.bidBeginner))) {
                    pass++;
                    if (this.bidBeginner.getId() == 4)
                        this.bidBeginner = this.allPlayers.get(0);
                    else
                        this.bidBeginner = this.allPlayers.get(this.bidBeginner.getId());
                } else {
                    hasBid = this.resuggerBid(this.bidBeginner);
                }
            }
            try {
                Thread.sleep(500);

            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
