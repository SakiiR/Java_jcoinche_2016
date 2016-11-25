import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by sakiir on 14/11/16.
 */

public class                            GameHandle {
    private ArrayList<GameThread>       gameThreads = null;
    private ArrayList<Thread>           threads = null;
    private ArrayList<JCoinchePlayer>   players;

    /**
     * Empty constructor
     */
    public                              GameHandle() {
        this.gameThreads = new ArrayList<>();
        this.players = new ArrayList<>();
        this.threads = new ArrayList<>();
    }

    /**
     * Stop the game thread
     */
    public void                         stopGame(GameThread gameThread) {
        Thread                          t = this.getThreadByGameThread(gameThread);

        if (t != null) {
            try {
                gameThread.setRunning(false);
                JCoincheUtils.logWarning("[!] Before Join " + gameThread.toString());
                t.join();
                JCoincheUtils.logWarning("[!] After Join " + gameThread.toString());
                // clean Game Thread
                // put gamethread players in the waiting queue
                for (JCoinchePlayer p : gameThread.getAllPlayers()) {
                    this.getPlayers().remove(p);
                    this.getPlayers().add(p);
                    p.setGameThread(null);
                    JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGameStoppedMessage());
                }
                // removing Thread and gamethread from list
                this.threads.remove(t);
                this.gameThreads.remove(gameThread);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private Thread                      getThreadByGameThread(GameThread gameThread) {
        int                             i = 0;

        for (GameThread gt : this.gameThreads) {
            if (gt == gameThread)
                return this.threads.get(i);
            ++i;
        }
        return null;
    }

    /**
     * Start the game thread
     */
    public void                         startGame(ArrayList<JCoinchePlayer> players) {
        GameThread                      gT = new GameThread(players);
        Thread                          t = new Thread(gT);

        JCoincheUtils.logWarning("[!] Starting Game With %d players", players.size());
        this.threads.add(t);
        this.gameThreads.add(gT);
        t.start();
    }

    public void                         handleGames() {
        int                             playersCount = this.players.size();

        if (playersCount == 0) {
            return;
        }

        // We have to start the new game
        if (playersCount % 4 == 0) {
            int index = 0;
            for (JCoinchePlayer p : this.players) {
                if (p.getGameThread() == null) {
                    break;
                }
                ++index;
            }
            this.startGame(new ArrayList<>(this.players.subList(index, index + 4)));
        }
    }

    public void                         handleRemainingsClients() {
        ArrayList<JCoinchePlayer>       waitingPlayers = new ArrayList<>();

        if (this.countWaitingClients() >= 3) {
            int j = 0;
            for (int i = 0 ; i < this.getPlayers().size() && j < 4 ; ++i) {
                if (this.getPlayers().get(i).getGameThread() == null) {
                    waitingPlayers.add(this.getPlayers().get(i));
                    ++j;
                }
            }
            JCoincheUtils.logWarning("[!] Waiting Clients : %d%s", waitingPlayers.size(), (j == 4 ? ", Starting Game With Them !" : ""));
            if (j == 4) {
                this.startGame(waitingPlayers);
            }
        }
    }

    private int                         countWaitingClients() {
        int                             j = 0;

        for (int i = 0 ; i < this.getPlayers().size() ; ++i) {
            if (this.getPlayers().get(i).getGameThread() == null)
                ++j;
        }
        return j;
    }

    /**
     * Return the list of gamethread
     * @return GameThread
     */
    public ArrayList<GameThread>        getGameThreads() {
        return this.gameThreads;
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
