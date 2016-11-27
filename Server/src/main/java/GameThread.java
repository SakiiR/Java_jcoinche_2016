import io.netty.channel.group.ChannelGroup;
import io.netty.channel.Channel;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by sakiir on 14/11/16.
 */

/**
 * This class is the main game handler started as thread
 * from GameHandle.
 *
 *  @see GameHandle
 */
public class                            GameThread implements Runnable {
    private boolean                     isRunning = true;
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   allPlayers = null;
    private CardGenerator               cardGenerator = null;
    private JCoincheBid                 bid = null;
    private JCoinchePlayer              generalBeginner = null;
    private JCoincheRound               round = null;
    private GameHandle                  gameHandle = null;
    private String                      uniqueId = null;

    /**
     * GameThread Constructor
     *
     * @param players The list of 4 players : who are starting a game
     */
    public                              GameThread(ArrayList<JCoinchePlayer> players, GameHandle gameHandle) {
        this.teams = new ArrayList<JCoincheTeam>();
        this.cardGenerator = new CardGenerator();
        this.isRunning = true;
        this.allPlayers = players;
        this.gameHandle = gameHandle;
        for (JCoinchePlayer p : this.allPlayers) {
            p.setGameThread(this);
        }
        this.uniqueId = new BigInteger(130, new SecureRandom()).toString(32);
    }

    /**
     * Retreive unique id
     *
     * @return unique id
     */
    public String                       getUniqueId() {
        return uniqueId;
    }

    /**
     * Set the unique Id
     *
     * @param uniqueId The unique Id
     * @return GameThread instance
     */
    public GameThread                   setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    /**
     * Is the game running.
     *
     * @return a bool specifying the running status.
     */
    public boolean                      isRunning() {
        return isRunning;
    }

    /**
     * Set the running boolean
     *
     * @param running boolean value
     * @return GameThread instance
     */
    public GameThread                   setRunning(boolean running) {
        isRunning = running;
        return this;
    }

    /**
     * Get the card generator
     * @return a card Generator instance
     */
    public CardGenerator                getCardGenerator() {
        return cardGenerator;
    }

    /**
     * Get the general beginner

     * @return the general beginner JCoinchePlayer
     */
    public JCoinchePlayer               getGeneralBeginner() {
        return generalBeginner;
    }

    /**
     * Main GameThread loop
     *
     * @see GameHandle
     */
    @Override
    public void                         run() {
        JCoincheUtils.logInfo("[!] Entering Main GameThread::run() Method");
        this.initializeTeams();
        this.generalBeginner = this.allPlayers.get(0);
        this.bid = new JCoincheBid(this);
        while (!this.checkScoreTeams() && this.isRunning) {
            this.bid.setBeginner(this.generalBeginner).runBid();
            if (!this.isRunning) return;
            this.sendBidToPlayers();
            //on entre dans la boucle de plis
            round = new JCoincheRound(this.bid.getBidInformations(), this.generalBeginner, this.teams, this.allPlayers, this);
            round.run();
            //fin de boucle change le beginner général
            if (this.generalBeginner.getId() == 4)
                this.generalBeginner = this.allPlayers.get(0);
            else
                this.generalBeginner = this.allPlayers.get(generalBeginner.getId());
            try {
                Thread.sleep(500);
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!this.isRunning) { return; }
        this.sendResultToPlayers();
        if (this.isRunning) {
            for (JCoinchePlayer p : this.allPlayers) {
                p.getChannel().disconnect();
                p.setChannel(null);
            }
            this.gameHandle.removeInnactiveChannels();
        }
        JCoincheUtils.logSuccess("[+] Definitly ending GameThread::run()");
    }

    /**
     * Send the final Game results to players
     *
     * @see GameThread#run()
     */
    private void                        sendResultToPlayers() {
        JCoincheTeam                    winnerTeam;
        JCoincheTeam                    looserTeam;

        if (this.teams.get(0).getScore() > this.teams.get(1).getScore()) {
            winnerTeam = this.teams.get(0);
            looserTeam = this.teams.get(1);
        } else {
            winnerTeam = this.teams.get(1);
            looserTeam = this.teams.get(0);
        }
        for (JCoinchePlayer p : this.allPlayers) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeEndGameMessage(winnerTeam.getId(), winnerTeam.getScore(),
                    looserTeam.getId(), looserTeam.getScore()));
        }
    }

    /**
     * Retrieve all players playing
     *
     * @return the list of players that are playing.
     */
    public ArrayList<JCoinchePlayer>    getAllPlayers() {
        return allPlayers;
    }

    /**
     * Initialise the game.
     *
     * @see GameThread#run()
     */
    private void                        initializeTeams() {
        // todo: Be careful with Player ( cross table  1 with 3 && 2 with 4)
        // todo: may you can review this
        this.teams = new ArrayList<JCoincheTeam>();

        for (int i = 0; i < 4; ++i) {
            this.allPlayers.get(i)
                    .setId(i + 1)
                    .setMessage(null);
        }

        ArrayList<JCoinchePlayer> team1 = new ArrayList<>();
        team1.add(allPlayers.get(0));
        team1.add(allPlayers.get(2));
        this.teams.add(new JCoincheTeam(team1, 1));
        ArrayList<JCoinchePlayer> team2 = new ArrayList<>();
        team2.add(allPlayers.get(1));
        team2.add(allPlayers.get(3));
        this.teams.add(new JCoincheTeam(team2, 2));
        //todo: Broadcast GAME_START to all players
        for (JCoinchePlayer p : this.allPlayers) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeGameStartMessage(
                    p.getToken(),
                    p.getId(),
                    p.getTeam().getId(),
                    p.getPartner().getId(),
                    this.getUniqueId())
            );
        }
    }

    /**
     * Check the score of both team to know if the
     * game is over.
     *
     * @return a boolean
     */
    private boolean                     checkScoreTeams() {
        int                             score1;
        int                             score2;

        score1 = this.teams.get(0).getScore();
        score2 = this.teams.get(1).getScore();
        return ((score1 >= 400 || score2 >= 400) && score1 != score2);
    }

    /**
     * Send the bid information to all players
     *
     * @see GameThread#run()
     */
    private void                        sendBidToPlayers() {
        for (JCoinchePlayer p : this.allPlayers) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendBidInfoMessage(this.bid.getBidInformations()));
        }
    }

    /**
     * Get the array teams
     * @return an array of teams
     */
    public ArrayList<JCoincheTeam>      getTeams() {
        return teams;
    }
}