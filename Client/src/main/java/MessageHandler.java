/**
 * Created by sakiir on 19/11/16.
 */
public class        MessageHandler {

    public          MessageHandler() {

    }

    public void     parseMessage(JCoincheProtocol.JCoincheMessage message) {
        switch (message.getType()) {
            case WELCOME:
                handleWelcomeMessage(message.getMessageWelcome());
                break;
            default:
                System.out.println("[>] Unknow message type ..");
                break;
        }
    }

    private void    handleWelcomeMessage(JCoincheProtocol.WelcomeMessage message) {
        System.out.println("[>] Handling welcome Message");
    }
}
