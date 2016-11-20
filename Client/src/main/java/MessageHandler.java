/**
 * Created by sakiir on 19/11/16.
 */
public class                MessageHandler {

    private ClientProcess   clientProcess;

    public          MessageHandler(ClientProcess clientProcess) {
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

    private void handleGetCardsMessage(JCoincheProtocol.GetCardsMessage message) {
        System.out.println(String.format("[>] GET_CARDS Message !"));
        System.out.println(String.format("[>] My Cards :"));
        // tmp dump
        for (int i = 0 ; i < message.getColorsCount() ; ++i) {
            System.out.println(String.format("{id : %d, color : %d}", message.getIds(i), message.getColors(i)));
        }
    }
}
