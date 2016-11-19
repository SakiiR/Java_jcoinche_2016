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
            default:
                System.out.println("[>] Unknow message type ..");
                break;
        }
    }

    private void    handleWelcomeMessage(JCoincheProtocol.WelcomeMessage message) {
        System.out.println(String.format("[>] Handling welcome Message {type : \"WELCOME\", message : \"%s\"}", message.getMessage()));
    }
}
