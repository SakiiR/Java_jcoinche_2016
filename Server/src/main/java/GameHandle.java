import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 * Created by sakiir on 14/11/16.
 */

public class                GameHandle {
    protected GameThread    gameThread = null;
    protected ChannelGroup  channels = null;

    /**
     * Empty constructor
     */
    public                  GameHandle() {  }

    /**
     * Is the game running ?
     * @return
     */
    public boolean          isRunning() {
        return (this.gameThread != null);
    }

    /**
     * Set the channels object (ChannelGroup)
     * @param channels
     * @return GameHandle
     */
    public GameHandle       setChannels(ChannelGroup channels) {
        this.channels = channels;
        return this;
    }

    /**
     * Send a message to all channel/clients
     * @param message
     */
    public void             sendToAllChannel(String message) {
        for (Channel ch : this.channels) {
            ch.writeAndFlush(message);
        }
    }

    /**
     * Stop the game thread
     */
    public void             stopGame() {
        System.out.println(JCoincheConstants.log_game_stopped);
        this.gameThread.stopGame();
        this.gameThread = null;
    }

    /**
     * Start the game thread
     */
    public void             startGame() {
        System.out.println(JCoincheConstants.log_game_started);
        this.gameThread = new GameThread(this.channels, this);
        Thread t  = new Thread(this.gameThread);
        t.start();
    }

    /**
     * Return the game thread
     * @return GameThread
     */
    public GameThread       getGameThread() {
        return this.gameThread;
    }
}
