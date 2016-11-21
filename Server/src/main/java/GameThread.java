import io.netty.channel.group.ChannelGroup;
import io.netty.channel.Channel;
import java.util.ArrayList;

/**
 * Created by sakiir on 14/11/16.
 */
public class                            GameThread implements Runnable {
    public static boolean               isRunning = true;
    private ChannelGroup                channelGroup = null;
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   allPlayers = null;
    private CardGenerator               cardGenerator = null;
    private JCoincheBid                 bid = null;
    private JCoinchePlayer              generalBeginner = null;

    /**
     * GameThread Constructor
     *
     * @param channelGroup
     */
    public GameThread(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
        this.teams = new ArrayList<JCoincheTeam>();
        this.cardGenerator = new CardGenerator();
        GameThread.isRunning = true;
    }

    /**
     * run() method for the game thread
     */
    @Override
    public void run() {
        this.initializeTeams();
        this.generalBeginner = this.allPlayers.get(0);
        this.bid = new JCoincheBid(teams, allPlayers, generalBeginner, cardGenerator);
        while (!this.checkScoreTeams() && GameThread.isRunning) {
            this.bid.setBeginner(this.generalBeginner).runBid();
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
    }

    public ArrayList<JCoinchePlayer> getAllPlayers() {
        return allPlayers;
    }

    private ArrayList<Channel> channelGroupToArrayList() {
        ArrayList<Channel> channels = new ArrayList<>();

        for (Channel ch : this.channelGroup) {
            channels.add(ch);
        }
        return channels;
    }

    private void initializeTeams() {
        ArrayList<Channel> n_channels = this.channelGroupToArrayList();

        this.allPlayers = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            JCoinchePlayer player = new JCoinchePlayer(n_channels.get(i), i + 1);
            allPlayers.add(player);
        }

        // todo: Be careful with Player ( cross table  1 with 3 && 2 with 4)
        // todo: may you can review this
        this.teams = new ArrayList<JCoincheTeam>();

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

    private boolean checkScoreTeams() {
        int score1;
        int score2;

        score1 = this.teams.get(0).getScore();
        score2 = this.teams.get(1).getScore();
        return ((score1 >= 1000 || score2 >= 1000) && score1 != score2);
    }

    private void broadcastGameStart() {

    }

    /**
     * stop the game thread
     *
     * @return GameThread
     */
    public GameThread stopGame() {
        this.isRunning = false;
        return this;
    }
}