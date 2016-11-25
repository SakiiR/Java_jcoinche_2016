import io.netty.channel.group.ChannelGroup;
import io.netty.channel.Channel;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by sakiir on 14/11/16.
 */
public class                            GameThread implements Runnable {
    public static boolean               isRunning = true;
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   allPlayers = null;
    private CardGenerator               cardGenerator = null;
    private JCoincheBid                 bid = null;
    private JCoinchePlayer              generalBeginner = null;
    private JCoincheRound               round = null;

    /**
     * Constructor
     * @param players
     */
    public                              GameThread(ArrayList<JCoinchePlayer> players) {
        this.teams = new ArrayList<JCoincheTeam>();
        this.cardGenerator = new CardGenerator();
        GameThread.isRunning = true;
        this.allPlayers = players;
    }

    /**
     * run() method for the game thread
     */
    @Override
    public void                         run() {
        this.initializeTeams();
        this.generalBeginner = this.allPlayers.get(0);
        this.bid = new JCoincheBid(teams, allPlayers, generalBeginner, cardGenerator);
        while (!this.checkScoreTeams() && GameThread.isRunning) {
            this.bid.setBeginner(this.generalBeginner).runBid();
            if (!GameThread.isRunning) return;
            this.sendBidToPlayers();
            //on entre dans la boucle de plis
            round = new JCoincheRound(this.bid.getBidInformations(), this.generalBeginner, this.teams, this.allPlayers);
            round.run();
            //fin de boucle change le beginner général
            if (this.generalBeginner.getId() == 4)
                this.generalBeginner = this.allPlayers.get(0);
            else
                this.generalBeginner = this.allPlayers.get(generalBeginner.getId());
            try {
                Thread.sleep(500);
                JCoincheUtils.log("[>] Bid okay return inside runBid");
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!GameThread.isRunning) { return; }
        this.sendResultToPlayers();
        JCoincheUtils.logSuccess("[+] Definitly ending GameThread::run()");
    }

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

    public ArrayList<JCoinchePlayer>    getAllPlayers() {
        return allPlayers;
    }

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
                    p.getPartner().getId())
            );
        }
    }

    private boolean                     checkScoreTeams() {
        int                             score1;
        int                             score2;

        score1 = this.teams.get(0).getScore();
        score2 = this.teams.get(1).getScore();
        return ((score1 >= 1000 || score2 >= 1000) && score1 != score2);
    }

    private void                        sendBidToPlayers() {
        for (JCoinchePlayer p : this.allPlayers) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendBidInfoMessage(this.bid.getBidInformations()));
        }
    }
}