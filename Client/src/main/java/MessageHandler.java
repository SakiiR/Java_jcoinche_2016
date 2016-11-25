import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by sakiir on 19/11/16.
 */
public class                            MessageHandler {
    private ClientProcess               clientProcess;

    public                              MessageHandler(ClientProcess clientProcess) {
        this.clientProcess = clientProcess;
    }

    public void                         parseMessage(JCoincheProtocol.JCoincheMessage message) {
        switch (message.getType()) {
            case WELCOME:
                this.handleWelcomeMessage(message.getWelcomeMessage());
                break;
            case GAME_START:
                this.handleGameStartMessage(message.getGameStartMessage());
                break;
            case GET_CARDS:
                this.handleGetCardsMessage(message.getGetCardsMessage());
                break;
            case GET_BID:
                this.handleGetBidMessage(message.getGetBidMessage());
                break;
            case ERROR:
                this.handleErrorMessage(message.getErrorMessage());
                break;
            case SEND_BID:
                this.handleSendBidMessage(message.getSendBidMessage());
                break;
            case GET_COINCHE:
                this.handleGetCoincheMessage(message.getGetCoincheMessage());
                break;
            case SEND_COINCHE:
                this.handleSendCoincheMessage(message.getSendCoincheMessage());
                break;
            case GET_SURCOINCHE:
                this.handleGetSurcoincheMessage(message.getGetSurcoincheMessage());
                break;
            case SEND_SURCOINCHE:
                this.handleSendSurcoincheMessage(message.getSendSurcoincheMessage());
                break;
            case GAME_STOPPED:
                this.handleGameStoppedMessage(message.getGameStoppedMessage());
                break;
            case SEND_BID_INFO:
                this.handleSendBidInformationsMessage(message.getSendBidInfoMessage());
                break;
            case START_TRICK:
                this.handleStartTrickMessage(message.getStartTrickMessage());
                break;
            case GET_CARD:
                this.handleGetCardMessage(message.getGetCardMessage());
                break;
            case SEND_CARD:
                this.handleSendCardMessage(message.getSendCardMessage());
                break;
            case SEND_WIN_TRICK:
                this.handleSendWinTrickMessage(message.getSendWinTrickMessage());
                break;
            case SEND_WIN_ROUND:
                this.handleSendWinRoundMessage(message.getSendWinRoundMessage());
                break;
            default:
                JCoincheUtils.logInfo("[>] Unknow Message received  [%s] ..", message.getType());
                break;
        }
    }

    private int                         promptInt(String message) {
        int                             result = -1;
        boolean                         validInput = false;
        String                          input;
        Scanner                         s = new Scanner(System.in);

        while (!validInput) {
            JCoincheUtils.logSuccess(message);
            try {
                input = s.nextLine().trim();
            } catch (Exception e) {
                s = new Scanner(System.in);
                input = "-1";
            }
            try {
                result = Integer.parseInt(input);
            } catch (Exception e) { }
            if (result >=  0) {
                return result;
            }
        }
        return result;
    }

    private void                        handleWelcomeMessage(JCoincheProtocol.WelcomeMessage message) {
        JCoincheUtils.logInfo("[>] WELCOME Message {type : \"WELCOME\", message : \"%s\"}", message.getMessage());
    }

    private void                        handleGameStartMessage(JCoincheProtocol.GameStartMessage message) {
        JCoincheUtils.logInfo("[>] The Game Is Starting ! PLAYER(%d) TEAM(%d)", message.getPlayerId(), message.getTeamId());
        this.clientProcess.getPlayerInformations()
                .setToken(message.getToken())
                .setPlayerId(message.getPlayerId())
                .setTeamId(message.getTeamId());
    }

    private void                        handleGetCardsMessage(JCoincheProtocol.GetCardsMessage message) {
        this.clientProcess.getPlayerInformations().getCards().clear();
        this.clientProcess.getPlayerInformations().setCards(new ArrayList<JCoincheCard>());
        for (int i = 0 ; i < message.getColorsCount() ; ++i) {
            this.clientProcess.getPlayerInformations().getCards().add(new JCoincheCard(
                    JCoincheCard.Color.valueOf(JCoincheCard.Color.values()[message.getColors(i)].name()),
                    JCoincheCard.Id.valueOf(JCoincheCard.Id.values()[message.getIds(i)].name())
            ));
        }
        this.clientProcess.getPlayerInformations().dumpCard();
    }

    private void                        handleGetBidMessage(JCoincheProtocol.GetBidMessage message) {
        int                             bidValue = -1;
        int                             trump = -1;

        while (!((bidValue >= message.getValue() || bidValue == 0 ) && bidValue % 10 == 0)) {
            bidValue = this.promptInt(String.format("[>] Please .. Enter Your bid ( Between %d and 170 -> 10 by 10) or 0 to pass :", message.getValue()));
        }

        if (bidValue >= message.getValue()) {
            while (!(trump >= 0 && trump <= 5)) {
                trump = this.promptInt("[>] Now .. Enter your trump {0: HEART, 1: DIAMOND, 2: CLUB, 3: SPADE, 4: WT, 5: AT} :");
            }
        }

        if (bidValue == 0) {
            JCoincheUtils.writeAndFlush(
                    this.clientProcess.getPlayerInformations().getChannel(),
                    MessageForger.forgeSetBidMessage(this.clientProcess.getPlayerInformations().getToken())
                    );
        } else {
            JCoincheUtils.writeAndFlush(this.clientProcess.getPlayerInformations().getChannel(),
                    MessageForger.forgeSetBidMessage(this.clientProcess.getPlayerInformations().getToken(),
                            bidValue,
                            trump)
            );
        }
    }

    private void                        handleErrorMessage(JCoincheProtocol.ErrorMessage message) {
        JCoincheUtils.logError("[-] Error : %s", message.getMessage());
    }

    private void                        handleSendBidMessage(JCoincheProtocol.SendBidMessage message) {
        String                          who = (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId() ? "I" : String.format("Player [%d]", message.getPlayerId()));
        String                          value = (message.getBidValue() == 170 ? "CAPOT" : String.format("%d", message.getBidValue()));

        if (message.getBid()) {
            JCoincheUtils.logSuccess("[BID] %s bid for -> %s on %s", who, value, EnumUtils.getTrumpTypeByIndex(message.getBidTrump()));
        } else {
            JCoincheUtils.logSuccess("[BID] %s pass ..", who);
        }
    }

    private void                        handleGetCoincheMessage(JCoincheProtocol.GetCoincheMessage message) {
        int                             coincheOrNot = -1;

        while (!(coincheOrNot >= 0 && coincheOrNot <= 1)) {
            coincheOrNot = this.promptInt("[>] Do you want to Coinche ? {0: No, 1: Yes}");
        }
        JCoincheUtils.writeAndFlush(this.clientProcess.getPlayerInformations().getChannel(),
                MessageForger.forgeSetCoincheMessage(
                        this.clientProcess.getPlayerInformations().getToken(),
                        (coincheOrNot == 0 ? false : true)
                )
        );
    }

    private void                        handleSendCoincheMessage(JCoincheProtocol.SendCoincheMessage message) {
        if (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId()) {
            JCoincheUtils.logSuccess("[>] I Coinche !");
        } else {
            JCoincheUtils.logSuccess("[>] Player [%d] Coinche !", message.getPlayerId());
        }
    }

    private void                        handleGetSurcoincheMessage(JCoincheProtocol.GetSurcoincheMessage message) {
        int                             surcoincheOrNot = -1;

        while (!(surcoincheOrNot >= 0 && surcoincheOrNot <= 1)) {
            surcoincheOrNot = this.promptInt("[>] Do you want to surcoinche ? {0: No, 1: Yes}");
        }
        JCoincheUtils.writeAndFlush(this.clientProcess.getPlayerInformations().getChannel(),
                MessageForger.forgeSetSurcoincheMessage(
                        this.clientProcess.getPlayerInformations().getToken(),
                        (surcoincheOrNot != 0)
                )
        );
    }

    private void                        handleSendSurcoincheMessage(JCoincheProtocol.SendSurcoincheMessage message) {
        if (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId()) {
            JCoincheUtils.logSuccess("[>] I Surcoinche !");
        } else {
            JCoincheUtils.logSuccess("[>] Player [%d] Surcoinche !", message.getPlayerId());
        }
    }

    private void                        handleGameStoppedMessage(JCoincheProtocol.GameStoppedMessage message) {
        JCoincheUtils.logWarning("[!] Game has been stopped ! waiting for a new player ..");
    }

    private void                        handleSendBidInformationsMessage(JCoincheProtocol.SendBidInfoMessage message) {
        String                          value = (message.getValue() == 170 ? "CAPOT" : String.format("%d", message.getValue()));
        String                          trump = EnumUtils.getTrumpTypeByIndex(message.getTrump()).name();
        String                          who = (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId() ? "I" : String.format("Player [%d]", message.getPlayerId()));

        this.clientProcess.getPlayerInformations().setBidValue(value).setBidTrump(trump);
        JCoincheUtils.logSuccess("[+] %s Set the \"contrat\" to %s of %s ..", who, value, trump);
    }

    private void                        handleStartTrickMessage(JCoincheProtocol.StartTrickMessage message) {
        JCoincheUtils.logSuccess("[+] Starting Trick N°%d", message.getTrickNumber());
    }

    private void                        handleGetCardMessage(JCoincheProtocol.GetCardMessage message) {
        int                             cardIndex = -1;
        JCoincheCard                    toSend = null;

        this.clientProcess.getPlayerInformations().dumpCardWithIndex();
        JCoincheUtils.logSuccess("[BID] The Bid / Trump are : %s of %s",
                this.clientProcess.getPlayerInformations().getBidValue(),
                this.clientProcess.getPlayerInformations().getBidTrump()
        );
        while (!(cardIndex >= 0 && cardIndex < this.clientProcess.getPlayerInformations().getCards().size())) {
            cardIndex = this.promptInt("[+] Please .. Choose a Card : ");
        }
        toSend = this.clientProcess.getPlayerInformations().getCards().get(cardIndex);
        JCoincheUtils.writeAndFlush(this.clientProcess.getPlayerInformations().getChannel(), MessageForger.forgeSetCardMessage(
                this.clientProcess.getPlayerInformations().getToken(),
                toSend
        ));
    }

    private void                        handleSendCardMessage(JCoincheProtocol.SendCardMessage message) {
        String                          who = (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId() ? "I" : String.format("Player [%d]", message.getPlayerId()));
        JCoincheCard                    toDelete = null;

        JCoincheUtils.logSuccess("[+] %s dropped Card %s of %s",
                who,
                EnumUtils.getIdByIndex(message.getCardId()),
                EnumUtils.getColorByIndex(message.getCardColor()));
        if (this.clientProcess.getPlayerInformations().getLastCardPlayed() == null) {
            this.clientProcess.getPlayerInformations().setLastCardPlayed(new JCoincheCard(EnumUtils.getColorByIndex(message.getCardColor()), EnumUtils.getIdByIndex(message.getCardId())));
        }
        if (who == "I") {
            for (JCoincheCard c : this.clientProcess.getPlayerInformations().getCards()) {
                if (c.getColor().ordinal() == message.getCardColor() &&
                        c.getId().ordinal() == message.getCardId()) {
                    toDelete = c;
                }
            }
            this.clientProcess.getPlayerInformations().getCards().remove(toDelete);
        }
    }

    private void                        handleSendWinTrickMessage(JCoincheProtocol.SendWinTrickMessage message) {
        String                          who = (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId() ? "I" : String.format("Player [%d]", message.getPlayerId()));
        String                          team = (who == "I" ? "" : String.format(" from team %d", message.getTeamId()));

        JCoincheUtils.logSuccess("[+] %s%s won the Trick ! with SCORE : %d", who, team, message.getScore());
        this.clientProcess.getPlayerInformations().setLastCardPlayed(null);
    }

    private void                        handleSendWinRoundMessage(JCoincheProtocol.SendWinRoundMessage message) {
        JCoincheUtils.logSuccess(message.getMessage());

        JCoincheUtils.logSuccess("--------------------");
        if (message.getBidderTeamId() == this.clientProcess.getPlayerInformations().getTeamId()) {
            JCoincheUtils.logSuccess("[+] My Team Did %d pts on this round !", message.getBidderTeamRoundScore());
            JCoincheUtils.logSuccess("[+] My Team has %d pts At This Moment !", message.getBidderTeamScore());
            JCoincheUtils.logSuccess("[+] Other Team Did %d pts on this round !", message.getOtherTeamRoundScore());
            JCoincheUtils.logSuccess("[+] Other Team Has %d pts At This Moment !", message.getOtherTeamScore());
        } else {
            JCoincheUtils.logSuccess("[+] My Team Did %d pts on this round !", message.getOtherTeamRoundScore());
            JCoincheUtils.logSuccess("[+] My Team has %d pts At This Moment !", message.getOtherTeamScore());
            JCoincheUtils.logSuccess("[+] Other Team Did %d pts on this round !", message.getBidderTeamRoundScore());
            JCoincheUtils.logSuccess("[+] Other Team Has %d pts At This Moment !", message.getBidderTeamScore());
        }
        JCoincheUtils.logSuccess("--------------------");
    }
}
