import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by sakiir on 14/11/16.
 */

public class                            GameHandle {
    private GameThread                  gameThread = null;
    ArrayList<JCoinchePlayer>           players;
    Thread                              t;

    /**
     * Empty constructor
     */
    public                              GameHandle() {
        this.players = new ArrayList<>();
    }

    /**
     * Is the game running ?
     * @return
     */
    public boolean                      isRunning() {
        return (GameThread.isRunning);
    }

    /**
     * Set the channels object (ChannelGroup)
     * @param channels
     * @return GameHandle
     */
    public GameHandle                   setChannels(ChannelGroup channels) {
        return this;
    }

    /**
     * Stop the game thread
     */
    public void                         stopGame() {
        JCoincheUtils.logWarning(JCoincheConstants.log_game_stopped);
        for (JCoinchePlayer p : this.gameThread.getAllPlayers()) {
            JCoincheUtils.writeAndFlushWithoutChecks(p.getChannel(), MessageForger.forgeGameStoppedMessage());
        }
        GameThread.isRunning = false;
        try {
            JCoincheUtils.logWarning("[!] Before join");
            this.t.join();
            this.removeInnactiveChannels();
            JCoincheUtils.logWarning("[!] After join");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the game thread
     */
    public void                         startGame() {
        this.removeInnactiveChannels();
        ArrayList<JCoinchePlayer> fourPlayers = new ArrayList<>(this.players.subList(0, 4));
        JCoincheUtils.logSuccess("[+] Starting Game with %d Players", fourPlayers.size());
        this.gameThread = new GameThread(fourPlayers);
        this.t  = new Thread(this.gameThread);
        this.t.start();
    }

    /**
     * Return the game thread
     * @return GameThread
     */
    public GameThread                   getGameThread() {
        return this.gameThread;
    }

    /**
     * Add Player to the queue
     * @return void
     */
    public void                         addPlayer(Channel channel) {
        this.players.add(new JCoinchePlayer(channel));
    }

    /**
     * Get A Player By his channel
     * @return JCoinchePlayer
     */
    public JCoinchePlayer               getPlayerByChannel(Channel channel) {
        for (JCoinchePlayer p : this.players) {
            if (channel == p.getChannel()) {
                return p;
            }
        }
        return null;
    }

    public void                         removeInnactiveChannels() {
        ArrayList<JCoinchePlayer>       toRemove = new ArrayList<>();

        for (JCoinchePlayer p : this.players) {
            if (p.getChannel() == null) {
                toRemove.add(p);
            }
        }
        for(JCoinchePlayer p : toRemove) {
            this.players.remove(p);
        }
    }
    /**
     * Return the size of players array
     * @return int
     */
    public int                          getPlayersCount() {
        int                             i = 0;

        for(JCoinchePlayer p : this.players) {
            if (p.getChannel() != null) {
                ++i;
            }
        }
        return i;
    }


    /**
     * Get Player List
     * @return
     */
    public ArrayList<JCoinchePlayer>    getPlayers() {
        return players;
    }

    /**
     * Set Player List
     * @param players
     * @return
     */
    public GameHandle                   setPlayers(ArrayList<JCoinchePlayer> players) {
        this.players = players;
        return this;
    }
}
