import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sakiir on 19/11/16.
 */

/**
 * This class is the main client behaviour definition.
 * It is used to do action relative to messages received.
 *
 * @see ClientProcess
 */
public class                            MessageHandler {
    private ClientProcess               clientProcess;

    /**
     * MessageHandler constructor
     *
     * @param clientProcess ClientProcess sending messages
     */
    public                              MessageHandler(ClientProcess clientProcess) {
        this.clientProcess = clientProcess;
    }

    /**
     * The big switch case to know what kind of message
     * it is and send it to the right method.
     *
     * @param message Message to Handle
     */
    public void                         parseMessage(JCoincheProtocol.JCoincheMessage message) {
        switch (message.getType()) {
            case WELCOME:
                if (message.hasWelcomeMessage()) this.handleWelcomeMessage(message.getWelcomeMessage());
                break;
            case GAME_START:
                if (message.hasGameStartMessage()) this.handleGameStartMessage(message.getGameStartMessage());
                break;
            case GET_CARDS:
                if (message.hasGetCardsMessage()) this.handleGetCardsMessage(message.getGetCardsMessage());
                break;
            case GET_BID:
                if (message.hasGetBidMessage()) this.handleGetBidMessage(message.getGetBidMessage());
                break;
            case ERROR:
                if (message.hasErrorMessage()) this.handleErrorMessage(message.getErrorMessage());
                break;
            case SEND_BID:
                if (message.hasSendBidMessage()) this.handleSendBidMessage(message.getSendBidMessage());
                break;
            case GET_COINCHE:
                if (message.hasGetCoincheMessage()) this.handleGetCoincheMessage(message.getGetCoincheMessage());
                break;
            case SEND_COINCHE:
                if (message.hasSendCoincheMessage()) this.handleSendCoincheMessage(message.getSendCoincheMessage());
                break;
            case GET_SURCOINCHE:
                if (message.hasGetSurcoincheMessage()) this.handleGetSurcoincheMessage(message.getGetSurcoincheMessage());
                break;
            case SEND_SURCOINCHE:
                if (message.hasSendCoincheMessage()) this.handleSendSurcoincheMessage(message.getSendSurcoincheMessage());
                break;
            case GAME_STOPPED:
                if (message.hasGameStoppedMessage()) this.handleGameStoppedMessage(message.getGameStoppedMessage());
                break;
            case SEND_BID_INFO:
                if (message.hasSendBidInfoMessage()) this.handleSendBidInformationsMessage(message.getSendBidInfoMessage());
                break;
            case START_TRICK:
                if (message.hasStartTrickMessage()) this.handleStartTrickMessage(message.getStartTrickMessage());
                break;
            case GET_CARD:
                if (message.hasGetCardMessage()) this.handleGetCardMessage(message.getGetCardMessage());
                break;
            case SEND_CARD:
                if (message.hasSendCardMessage()) this.handleSendCardMessage(message.getSendCardMessage());
                break;
            case SEND_WIN_TRICK:
                if (message.hasSendWinTrickMessage()) this.handleSendWinTrickMessage(message.getSendWinTrickMessage());
                break;
            case SEND_WIN_ROUND:
                if (message.hasSendWinRoundMessage()) this.handleSendWinRoundMessage(message.getSendWinRoundMessage());
                break;
            case END_GAME:
                if (message.hasEndGameMessage()) this.handleEndGameMessage(message.getEndGameMessage());
                break;
            default:
                JCoincheUtils.logInfo("[>] Unknow Message received  [%s] ..", message.getType());
                break;
        }
    }

    /**
     * Ask the used for an int in console input.
     *
     * @param message Message to Handle
     * @return int readed
     */
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

    /**
     * WELCOME Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleWelcomeMessage(JCoincheProtocol.WelcomeMessage message) {
        JCoincheUtils.logInfo("[>] WELCOME Message {type : \"WELCOME\", message : \"%s\"}", message.getMessage());
    }

    /**
     * GAME_START Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleGameStartMessage(JCoincheProtocol.GameStartMessage message) {
        JCoincheUtils.logInfo("[>] The Game Is Starting ! PLAYER(%d) TEAM(%d) UNIQUE_GAME_THREAD_ID(%s)", message.getPlayerId(), message.getTeamId(), message.getUniqueGameThreadId());
        this.clientProcess.getPlayerInformations()
                .setToken(message.getToken())
                .setPlayerId(message.getPlayerId())
                .setTeamId(message.getTeamId());
    }

    /**
     * GET_CARDS Message Handler
     *
     * @param message Message to Handle
     */
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

    /**
     * GET_BID Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleGetBidMessage(JCoincheProtocol.GetBidMessage message) {
        int                             bidValue = -1;
        int                             trump = -1;

        while (!((bidValue >= message.getValue() || bidValue == 0 ) && (bidValue % 10 == 0) && (bidValue <= 170))) {
            bidValue = this.promptInt(String.format("[>] Please .. Enter Your bid ( Between %d and 170 (CAPOT) -> 10 by 10) or 0 to pass :", message.getValue()));
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

    /**
     * ERROR Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleErrorMessage(JCoincheProtocol.ErrorMessage message) {
        JCoincheUtils.logError("[-] Error : %s", message.getMessage());
    }

    /**
     * SEND_BID Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleSendBidMessage(JCoincheProtocol.SendBidMessage message) {
        String                          who = (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId() ? "I" : String.format("Player [%d]", message.getPlayerId()));
        String                          value = (message.getBidValue() == 170 ? "CAPOT" : String.format("%d", message.getBidValue()));

        if (message.getBid()) {
            JCoincheUtils.logSuccess("[BID] %s bid for -> %s on %s", who, value, EnumUtils.getTrumpTypeByIndex(message.getBidTrump()));
        } else {
            JCoincheUtils.logSuccess("[BID] %s pass ..", who);
        }
    }

    /**
     * GET_COINCHE Message Handler
     *
     * @param message Message to Handle
     */
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

    /**
     * SEND_COINCHE Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleSendCoincheMessage(JCoincheProtocol.SendCoincheMessage message) {
        if (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId()) {
            JCoincheUtils.logSuccess("[>] I Coinche !");
        } else {
            JCoincheUtils.logSuccess("[>] Player [%d] Coinche !", message.getPlayerId());
        }
    }

    /**
     * GET_SURCOINCHE Message Handler
     *
     * @param message Message to Handle
     */
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

    /**
     * SEND_SURCOINCHE Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleSendSurcoincheMessage(JCoincheProtocol.SendSurcoincheMessage message) {
        if (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId()) {
            JCoincheUtils.logSuccess("[>] I Surcoinche !");
        } else {
            JCoincheUtils.logSuccess("[>] Player [%d] Surcoinche !", message.getPlayerId());
        }
    }

    /**
     * GAME_STOPPED Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleGameStoppedMessage(JCoincheProtocol.GameStoppedMessage message) {
        JCoincheUtils.logWarning("[!] Game has been stopped ! waiting for a new player ..");
    }

    /**
     * SEND_BID_INFO Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleSendBidInformationsMessage(JCoincheProtocol.SendBidInfoMessage message) {
        String                          value = (message.getValue() == 170 ? "CAPOT" : String.format("%d", message.getValue()));
        String                          trump = EnumUtils.getTrumpTypeByIndex(message.getTrump()).name();
        String                          who = (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId() ? "I" : String.format("Player [%d]", message.getPlayerId()));

        this.clientProcess.getPlayerInformations().setBidValue(value).setBidTrump(trump);
        JCoincheUtils.logSuccess("[+] %s Set the \"contrat\" to %s of %s ..", who, value, trump);
    }

    /**
     * START_TRICK Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleStartTrickMessage(JCoincheProtocol.StartTrickMessage message) {
        JCoincheUtils.logSuccess("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓", message.getTrickNumber());
        JCoincheUtils.logSuccess("▓ [+] Starting Trick N°%d    ▓", message.getTrickNumber());
        JCoincheUtils.logSuccess("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓", message.getTrickNumber());
    }

    /**
     * GET_CARD Message Handler : careful with GET_CARDS
     * message which is not the same.
     *
     * @param message Message to Handle
     */
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

    /**
     * SEND_CARD Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleSendCardMessage(JCoincheProtocol.SendCardMessage message) {
        String                          who = (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId() ? "I" : String.format("Player [%d]", message.getPlayerId()));
        JCoincheCard                    toDelete = null;

        JCoincheUtils.logInfo("[+] %s dropped Card %s of %s",
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

    /**
     * SEND_WIN_TRICK Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleSendWinTrickMessage(JCoincheProtocol.SendWinTrickMessage message) {
        String                          who = (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId() ? "I" : String.format("Player [%d]", message.getPlayerId()));
        String                          team = (who == "I" ? "" : String.format(" from team %d", message.getTeamId()));

        JCoincheUtils.logInfo("[+] %s%s won the Trick ! with SCORE : %d", who, team, message.getScore());
        this.clientProcess.getPlayerInformations().setLastCardPlayed(null);
    }

    /**
     * SEND_WIN_ROUND Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleSendWinRoundMessage(JCoincheProtocol.SendWinRoundMessage message) {
        JCoincheUtils.logSuccess(message.getMessage());

        JCoincheUtils.logSuccess("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
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
        JCoincheUtils.logSuccess("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
    }

    /**
     * END_GAME Message Handler
     *
     * @param message Message to Handle
     */
    private void                        handleEndGameMessage(JCoincheProtocol.EndGameMessage message) {
        if (message.getWinnerTeamId() == this.clientProcess.getPlayerInformations().getTeamId()) {
            JCoincheUtils.logSuccess("[+] My team (%d) won the game with %dpts", message.getWinnerTeamId(), message.getWinnerScore());
            JCoincheUtils.logSuccess("[+] Other team (%d) loose this game with %dpts", message.getLooserTeamId(), message.getLooserScore());
        } else {
            JCoincheUtils.logSuccess("[-] Other team (%d) won the game with %dpts", message.getWinnerTeamId(), message.getWinnerScore());
            JCoincheUtils.logSuccess("[-] My team (%d) loose this game with %dpts", message.getLooserTeamId(), message.getLooserScore());
        }
        JCoincheUtils.logSuccess("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
        JCoincheUtils.logWarning("[!] Game Over ! √ think about THE GAME :þ");
    }
}
