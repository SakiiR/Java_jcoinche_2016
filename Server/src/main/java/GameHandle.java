import io.netty.channel.group.ChannelGroup;

/**
 * Created by sakiir on 14/11/16.
 */

public class                GameHandle {
    protected Thread        thread = null;
    protected GameThread    gameThread = null;

    public                  GameHandle() {

    }

    public boolean          isRunning() {
        return (this.gameThread != null);
    }

    public void             stopGame() {
        System.out.println("[>] Stopping game..");
        this.gameThread.stopGame();
        this.gameThread = null;
        this.thread = null;
    }

    public void             startGame(ChannelGroup channelGroup) {
        System.out.println("[>] Starting game..");
        this.gameThread = new GameThread(channelGroup);
        this.thread = new Thread(this.gameThread);
        this.thread.start();
    }

    public GameThread       getGameThread() {
        return this.gameThread;
    }
}
