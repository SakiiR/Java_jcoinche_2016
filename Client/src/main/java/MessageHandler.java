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
                break;
            default:
                System.out.println("[>] Unknow message type ..");
                break;
        }
    }

    private void    handleWelcomeMessage(JCoincheProtocol.WelcomeMessage message) {
        System.out.println(String.format("[>] Handling welcome Message {type : \"WELCOME\", message : \"%s\"}", message.getMessage()));
    }

    private void    handleGameStartMessage(JCoincheProtocol.GameStartMessage message) {
        this.clientProcess.getPlayerInformations()
                .setToken(message.getToken())
                .setPlayerId(message.getPlayerId())
                .setTeamId(message.getTeamId());
    }
}
