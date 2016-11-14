import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 * Created by sakiir on 14/11/16.
 */
public class                GameHandle {
    protected GameThread    gameThread = null;
    protected ChannelGroup  channels = null;

    public                  GameHandle() {  }

    public boolean          isRunning() {
        return (this.gameThread != null);
    }

    public GameHandle       setChannels(ChannelGroup channels) {
        this.channels = channels;
        return this;
    }

    public void             sendToAllChannel(String message) {
        for (Channel ch : this.channels) {
            ch.writeAndFlush(message);
        }
    }

    public void             stopGame() {
        System.out.println(JCoincheConstants.log_game_stopped);
        this.gameThread.stopGame();
        this.gameThread = null;
    }

    public void             startGame() {
        System.out.println(JCoincheConstants.log_game_started);
        this.gameThread = new GameThread(this.channels, this);
        Thread t  = new Thread(this.gameThread);
        t.start();
    }

    public GameThread       getGameThread() {
        return this.gameThread;
    }
}
