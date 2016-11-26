import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sakiir on 19/11/16.
 */

/**
 * This class is the main client's game thread.
 * JCoincheClientHandler is giving message to it and
 * it is sending thoses message to the MessageHandler
 * It is a Thread btw :)
 *
 * @see JCoincheClientHandler
 * @see MessageHandler
 */
public class                                                    ClientProcess implements Runnable {

    private boolean                                             isRunning = true;
    private ArrayList<JCoincheProtocol.JCoincheMessage>         messages;
    private MessageHandler                                      messageHandler;
    private PlayerInformations                                  playerInformations;
    private Lock                                                lock;

    /**
     * Construction of the ClientProcess
     *
     * @see JCoincheClient
     */
    public                                                      ClientProcess() {
        this.messages = new ArrayList<>();
        this.messageHandler = new MessageHandler(this);
        this.playerInformations = new PlayerInformations();
        this.lock = new ReentrantLock();
    }

    /**
     * Main Loop method that sending message to
     * MessageHandler and cleaning theme just after
     * handling it.
     *
     * @see JCoincheClientHandler
     * @see MessageHandler
     * @see Thread
     */
    @Override
    public void                                                 run() {
        while (this.isRunning) {
            this.lock.lock();
            for (JCoincheProtocol.JCoincheMessage message : this.messages) {
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

    /**
     * This method is adding a message to the queue
     *
     * @see ArrayList
     * @param message
     * @return
     */
    public ClientProcess                                        addMessage(JCoincheProtocol.JCoincheMessage message) {
        this.lock.lock();
        this.messages.add(message);
        this.lock.unlock();
        return this;
    }

    /**
     * This method return the encapsulated local player
     * informations object reference.
     *
     * @return
     */
    public PlayerInformations                                   getPlayerInformations() {
        return this.playerInformations;
    }
}
