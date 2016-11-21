import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by sakiir on 19/11/16.
 */
public class                                                    ClientProcess implements Runnable {

    private boolean                                             isRunning = true;
    private ArrayList<JCoincheProtocol.JCoincheMessage>         messages;
    private MessageHandler                                      messageHandler;
    private PlayerInformations                                  playerInformations;

    public          ClientProcess() {
        this.messages = new ArrayList<JCoincheProtocol.JCoincheMessage>();
        this.messageHandler = new MessageHandler(this);
        this.playerInformations = new PlayerInformations();
    }

    @Override
    public void                                 run() {
        while (this.isRunning) {
            for (JCoincheProtocol.JCoincheMessage message : this.messages) {
                System.out.println(String.format(JCoincheConstants.log_last_message_handling, message.getType()));
                // send message to handler
                this.messageHandler.parseMessage(message);
            }

            if (this.messages.size() > 0) {
                this.messages.clear();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ClientProcess                        addMessage(JCoincheProtocol.JCoincheMessage message) {
        this.messages.add(message);
        return this;
    }

    public ClientProcess                        removeMessage(JCoincheProtocol.JCoincheMessage message) {
        this.messages.remove(message);
        return this;
    }

    public PlayerInformations                   getPlayerInformations() {
        return this.playerInformations;
    }
}
