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
            default:
                JCoincheUtils.log("[>] Unknow Message received  [%s] ..", message.getType());
                break;
        }
    }

    private int                         promptInt(String message) {
        int                             result = -1;
        boolean                         validInput = false;
        String                          input;
        Scanner                         s = new Scanner(System.in);

        while (!validInput) {
            JCoincheUtils.log(message);
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
        JCoincheUtils.log("[>] WELCOME Message {type : \"WELCOME\", message : \"%s\"}", message.getMessage());
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

        JCoincheUtils.log("[>] GET_BID Message !");
        while (!(bidValue >= message.getValue() || bidValue == 0)) {
            bidValue = this.promptInt(String.format("[>] Please .. Enter Your bid ( Between %d and 170 -> 10 by 10) or 0 to pass : ", message.getValue()));
        }

        if (bidValue >= message.getValue()) {
            while (!(trump >= 0 && trump <= 5)) {
                trump = this.promptInt("[>] Now .. Enter your trump {0: HEART, 1: DIAMOND, 2: CLUB, 3: SPADE, 4: WT, 5: AT} : ");
            }
        }

        JCoincheUtils.log("[>] Sending SET_BID with : {bidValue : %d, trump : %d}", bidValue, trump);
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
        JCoincheUtils.log("\n[>] SEND_BID Message");
        JCoincheUtils.log("[>] Bid Over ! Result :");
        if (message.getBid() == false) {
            if (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId()) {
                JCoincheUtils.log("\t[>] I pass", message.getPlayerId());
            } else {
                JCoincheUtils.log("\t[>] User %d pass ..", message.getPlayerId());
            }
            JCoincheUtils.log("[>] End Bid Infos ..\n");
            return;
        }
        JCoincheUtils.log("[>] \tBid Value : %d", message.getBidValue());
        JCoincheUtils.log("[>] \tBid Trump : %d", message.getBidTrumps());
        if (message.getPlayerId() == this.clientProcess.getPlayerInformations().getPlayerId()) {
            JCoincheUtils.log("[>] \tBy Me");
        } else {
            JCoincheUtils.log("[>] \tBy Player : %d", message.getPlayerId());
        }
        JCoincheUtils.log("[>] End Bid Infos ..\n");
    }

    private void                        handleGetCoincheMessage(JCoincheProtocol.GetCoincheMessage message) {
        int                             coincheOrNot = -1;

        while (!(coincheOrNot >= 0 && coincheOrNot <= 1)) {
            coincheOrNot = this.promptInt("[>] Do you want to Coinche ? {0: No, 1: Yes}");
        }
        MessageForger.forgeSetCoincheMessage(this.clientProcess.getPlayerInformations().getToken(), (coincheOrNot == 0 ? false : true));
    }
}
