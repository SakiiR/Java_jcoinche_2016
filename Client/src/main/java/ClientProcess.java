import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sakiir on 19/11/16.
 */
public class                                                    ClientProcess implements Runnable {

    private boolean                                             isRunning = true;
    private ArrayList<JCoincheProtocol.JCoincheMessage>         messages;
    private MessageHandler                                      messageHandler;
    private PlayerInformations                                  playerInformations;
    private Lock                                                lock;

    public          ClientProcess() {
        this.messages = new ArrayList<>();
        this.messageHandler = new MessageHandler(this);
        this.playerInformations = new PlayerInformations();
        this.lock = new ReentrantLock();
    }

    @Override
    public void                                 run() {
        while (this.isRunning) {
            this.lock.lock();
            for (JCoincheProtocol.JCoincheMessage message : this.messages) {
                System.out.println(String.format(JCoincheConstants.log_last_message_handling, message.getType()));
                // send message to handler
                this.messageHandler.parseMessage(message);
            }
            this.lock.unlock();
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
        this.lock.lock();
        this.messages.add(message);
        this.lock.unlock();
        return this;
    }

    public PlayerInformations                   getPlayerInformations() {
        return this.playerInformations;
    }
}
