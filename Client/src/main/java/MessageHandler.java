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
        System.out.println(String.format("[>] GAME_START Message : {token : %s, player_id : %d, team_id : %d}", message.getToken(), message.getPlayerId(), message.getTeamId()));
        this.clientProcess.getPlayerInformations()
                .setToken(message.getToken())
                .setPlayerId(message.getPlayerId())
                .setTeamId(message.getTeamId());
    }

    private void                        handleGetCardsMessage(JCoincheProtocol.GetCardsMessage message) {
        System.out.println(String.format("[>] GET_CARDS Message !"));
        System.out.println(String.format("[>] My Cards :"));

        for (int i = 0 ; i < message.getColorsCount() ; ++i) {
            System.out.println(String.format("{id : %d, color : %d}", message.getIds(i), message.getColors(i)));
        }
    }

    private void                        handleGetBidMessage(JCoincheProtocol.GetBidMessage message) {
        int                             bidValue = -1;
        int                             trump = -1;

        JCoincheUtils.logInfo("[>] GET_BID Message !");
        while (!((bidValue >= message.getValue() || bidValue == 0 ) && bidValue % 10 == 0)) {
            bidValue = this.promptInt(String.format("[>] Please .. Enter Your bid ( Between %d and 170 -> 10 by 10) or 0 to pass : ", message.getValue()));
        }

        if (bidValue >= message.getValue()) {
            while (!(trump >= 0 && trump <= 5)) {
                trump = this.promptInt("[>] Now .. Enter your trump {0: HEART, 1: DIAMOND, 2: CLUB, 3: SPADE, 4: WT, 5: AT} : ");
            }
        }

        JCoincheUtils.logInfo("[>] Sending SET_BID with : {bidValue : %d, trump : %d}", bidValue, trump);
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
        JCoincheUtils.log("[>] Error : %s", message.getMessage());
    }

    private void                        handleSendBidMessage(JCoincheProtocol.SendBidMessage message) {
        JCoincheUtils.logInfo("\n[>] SEND_BID Message");

        if (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId()) {
            if (message.getBid()) {
                JCoincheUtils.logWarning("\t[^] I bid for -> %d on %s", message.getBidValue(), (message.getBidTrump() > 3 ? EnumUtils.getTrumpTypeByIndex(message.getBidTrump() - 4) : EnumUtils.getColorByIndex(message.getBidTrump())));
            } else {
                JCoincheUtils.logWarning("\t[^] I pass ..");
            }
        } else {
            if (message.getBid()) {
                JCoincheUtils.logWarning("\t[^] Player [%d] bid for -> %d on %s", message.getPlayerId(), message.getBidValue(), (message.getBidTrump() > 3 ? EnumUtils.getTrumpTypeByIndex(message.getBidTrump() - 4) : EnumUtils.getColorByIndex(message.getBidTrump())));
            } else {
                JCoincheUtils.logWarning("\t[^] Player [%d] pass ..", message.getPlayerId());
            }
        }
        JCoincheUtils.logInfo("[>] END SEND_BID\n");
    }

    private void                        handleGetCoincheMessage(JCoincheProtocol.GetCoincheMessage message) {
        int                             coincheOrNot = -1;

        while (!(coincheOrNot >= 0 && coincheOrNot <= 1)) {
            coincheOrNot = this.promptInt("[>] Do you want to Coinche ? {0: No, 1: Yes}");
        }
        JCoincheUtils.logInfo("[>] SET_COINCHE %d!", coincheOrNot);
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

        JCoincheUtils.logInfo("[>] SET_SURCOINCHE %d!", surcoincheOrNot);
        JCoincheUtils.writeAndFlush(this.clientProcess.getPlayerInformations().getChannel(),
                MessageForger.forgeSetSurcoincheMessage(
                        this.clientProcess.getPlayerInformations().getToken(),
                        (surcoincheOrNot == 0 ? false : true))
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
}
