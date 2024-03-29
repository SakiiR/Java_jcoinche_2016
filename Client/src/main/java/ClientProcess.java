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

    private boolean                                             isRunning;
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
        this.isRunning = true;
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
        while (this.isRunning || messages.size() != 0) {
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
     * @param message Google Protocol Message
     * @return ClientProcess this
     */
    public ClientProcess                                        addMessage(JCoincheProtocol.JCoincheMessage message) {
        this.lock.lock();
        this.messages.add(message);
        this.lock.unlock();
        return this;
    }

    /**
     * Stop the client process thread
     */
    public void                                                 stopThread() {
        this.isRunning = false;
    }

    /**
     * Retrieve the message list.
     *
     * @return the message array list
     */
    public ArrayList<JCoincheProtocol.JCoincheMessage>          getMessages() {
        return this.messages;
    }

    /**
     * This method return the encapsulated local player
     * information object reference.
     *
     * @return PlayerInformation
     */
    public PlayerInformations                                   getPlayerInformations() {
        return this.playerInformations;
    }

    /**
     * This method set the encapsulated local player
     * information object reference.
     *
     * @param pI The player information struct
     * @return ClientProcess
     */
    public ClientProcess                                        setPlayerInformations(PlayerInformations pI) {
        this.playerInformations = pI;
        return this;
    }

    /**
     * Retrieve de message handler
     *
     * @return th message handler
     */
    public MessageHandler                                       getMessageHandler() {
        return this.messageHandler;
    }

}
