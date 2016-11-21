import java.util.Scanner;

/**
 * Created by sakiir on 19/11/16.
 */
public class                MessageHandler {
    private ClientProcess   clientProcess;
    private Scanner         s;

    public          MessageHandler(ClientProcess clientProcess) {
        this.s = new Scanner(System.in);
        this.clientProcess = clientProcess;
    }

    public void     parseMessage(JCoincheProtocol.JCoincheMessage message) {
        switch (message.getType()) {
            case WELCOME:
                handleWelcomeMessage(message.getWelcomeMessage());
                break;
            case GAME_START:
                handleGameStartMessage(message.getGameStartMessage());
                break;
            case GET_CARDS:
                handleGetCardsMessage(message.getGetCardsMessage());
                break;
            case GET_BID:
                handleGetBidMessage(message.getGetBidMessage());
                break;
            case ERROR:
                handleErrorMessage(message.getErrorMessage());
                break;
            default:
                System.out.println("[>] Unknow message type ..");
                break;
        }
    }

    private void    handleWelcomeMessage(JCoincheProtocol.WelcomeMessage message) {
        System.out.println(String.format("[>] WELCOME Message {type : \"WELCOME\", message : \"%s\"}", message.getMessage()));
    }

    private void    handleGameStartMessage(JCoincheProtocol.GameStartMessage message) {
        System.out.println(String.format("[>] GAME_START Message : {token : %s, player_id : %d, team_id : %d}", message.getToken(), message.getPlayerId(), message.getTeamId()));
        this.clientProcess.getPlayerInformations()
                .setToken(message.getToken())
                .setPlayerId(message.getPlayerId())
                .setTeamId(message.getTeamId());
    }

    private void    handleGetCardsMessage(JCoincheProtocol.GetCardsMessage message) {
        System.out.println(String.format("[>] GET_CARDS Message !"));
        System.out.println(String.format("[>] My Cards :"));

        for (int i = 0 ; i < message.getColorsCount() ; ++i) {
            System.out.println(String.format("{id : %d, color : %d}", message.getIds(i), message.getColors(i)));
        }
    }

    private void    handleGetBidMessage(JCoincheProtocol.GetBidMessage message) {
        System.out.println(String.format("[>] GET_BID Message !"));
        boolean     isValidInput = false;
        int         bidValue = 0;
        int         trump = 0;

        while (!isValidInput) {
            System.out.println(String.format("[>] Please .. Enter Your bid ( Between %d and 170 -> 10 by 10) or 0 to pass : "));
            bidValue = s.nextInt();
            if (bidValue > message.getValue() || bidValue == 0) {
                isValidInput = true;
            }
        }

        if (bidValue > message.getValue()) {
            isValidInput = false;
            while (!isValidInput) {
                System.out.println(String.format("[>] Now .. Enter your trump {0: HEART, 1: DIAMOND, 2: CLUB, 3: SPADE, 4: WT, 5: AT} : "));
                trump = s.nextInt();
                if (trump >= 0 && trump <= 5) {
                    isValidInput = true;
                }
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
                            trump));
        }
    }

    private void    handleErrorMessage(JCoincheProtocol.ErrorMessage message) {
        JCoincheUtils.log("[>] Error : %s", message.getMessage());
    }
}
