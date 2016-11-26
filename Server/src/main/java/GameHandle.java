import io.netty.channel.Channel;
import java.util.ArrayList;

/**
 * Created by sakiir on 14/11/16.
 */

/**
 * This class is used to handle all the threads like a Thread Pool.
 * It is able to launch a thread with 4 player, close a thread by
 * specifying his GameThread.
 * The Players list and it's initialisation is inside this class
 *
 * @see GameThread
 */
public class                            GameHandle {
    private ArrayList<GameThread>       gameThreads = null;
    private ArrayList<Thread>           threads = null;
    private ArrayList<JCoinchePlayer>   players;

    /**
     * The construction of the GameHandle
     */
    public                              GameHandle() {
        this.gameThreads = new ArrayList<>();
        this.players = new ArrayList<>();
        this.threads = new ArrayList<>();
    }

    /**
     * This method stop a thread by specifying his GameThread
     * and close all concerned clients.
     * It is also sending a protocol GAME_STOPPED Message
     *
     * @param gameThread
     * @see GameThread
     * @see MessageForger
     */
    public void                         stopGame(GameThread gameThread) {
        Thread                          t = this.getThreadByGameThread(gameThread);

        if (t != null) {
            try {
                gameThread.setRunning(false);
                JCoincheUtils.logWarning("[!] Before Join " + gameThread.toString());
                t.join();
                JCoincheUtils.logWarning("[!] After Join " + gameThread.toString());
                for (JCoinchePlayer p : gameThread.getAllPlayers()) {
                    this.getPlayers().remove(p);
                    this.getPlayers().add(p);
                    p.setGameThread(null);
                    JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGameStoppedMessage());
                }
                this.threads.remove(t);
                this.gameThreads.remove(gameThread);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method is getting a Thread by specifying his GameThread because
     * in this class, the gameThread array's element are at the same level
     * of the threads's elems.
     *
     * @param gameThread
     * @return Thread
     * @see GameThread
     * @see Thread
     */
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
     * This method is starting a game by specifying an
     * ArrayList of 4 players. A Thread is start and
     * added to the threads ArrayList
     *
     * @see ArrayList
     * @see GameThread
     * @see Thread
     */
    public void                         startGame(ArrayList<JCoinchePlayer> players) {
        GameThread                      gT = new GameThread(players, this);
        Thread                          t = new Thread(gT);

        JCoincheUtils.logWarning("[!] Starting Game With %d players", players.size());
        this.threads.add(t);
        this.gameThreads.add(gT);
        t.start();
    }

    /**
     * This method is called when a client is connected
     * to the server to check if it is needed to start a
     * game with the new clients. It is also calling startGame
     * if four players are idle.
     *
     * @see GameHandle#startGame(ArrayList)
     */
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

    /**
     * This method is used to check at the clients disconnection
     * to know if there is enought waiting clients to start a game (4).
     *
     * @see GameHandle#startGame(ArrayList)
     * @see GameHandle#countWaitingClients()
     * @see GameThread
     */
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

    /**
     * This method is used to count waiting clients connected
     * to the server. It is oftently used when a client is disconnected.
     *
     * @see GameThread
     * @see GameHandle#players
     * @return int
     */
    private int                         countWaitingClients() {
        int                             j = 0;

        for (int i = 0 ; i < this.getPlayers().size() ; ++i) {
            if (this.getPlayers().get(i).getGameThread() == null)
                ++j;
        }
        return j;
    }

    /**
     * This method return the list of the Launched Game Thread
     *
     * @return GameThread
     * @see GameThread
     */
    public ArrayList<GameThread>        getGameThreads() {
        return this.gameThreads;
    }

    /**
     * This method is used to  add a player to the list of players
     * by specifying an associated channel.
     *
     * @param channel
     * @return void
     * @see Channel
     * @see JCoinchePlayer
     */
    public void                         addPlayer(Channel channel) {
        this.players.add(new JCoinchePlayer(channel));
    }

    /**
     * This method is used to retreive a JCoinchePlayer
     * by specifying his associated channel. It is oftenly
     * used at the connection/disconnection of the client.
     *
     * @param channel
     * @return JCoinchePlayer
     * @see Channel
     * @see JCoinchePlayer
     */
    public JCoinchePlayer               getPlayerByChannel(Channel channel) {
        for (JCoinchePlayer p : this.players) {
            if (channel == p.getChannel()) {
                return p;
            }
        }
        return null;
    }

    /**
     * This method is used to remove all the
     * non-channel client from the list.
     *
     * @see ArrayList
     * @see JCoinchePlayer
     */
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
     * This method return the with-channel players count.
     *
     * @see JCoinchePlayer
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
     * This method return the JCoinchePlayer List.
     *
     * @see JCoinchePlayer
     * @see ArrayList
     */
    public ArrayList<JCoinchePlayer>    getPlayers() {
        return this.players;
    }

    /**
     * This method is setting the
     * player list to the object
     *
     * @see ArrayList
     * @see JCoinchePlayer
     * @param players
     * @return this
     */
    public GameHandle                   setPlayers(ArrayList<JCoinchePlayer> players) {
        this.players = players;
        return this;
    }
}
