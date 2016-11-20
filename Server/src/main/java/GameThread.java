import io.netty.channel.group.ChannelGroup;

import java.lang.reflect.Array;
import io.netty.channel.Channel;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sakiir on 14/11/16.
 */
public class                            GameThread implements Runnable {
    private boolean                     isRunning = true;
    private ChannelGroup                channelGroup = null;
    private List<String>                messages = null;
    private GameHandle                  gameHandle = null;
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   allPlayers = null;
    private CardGenerator               cardGenerator =null;
    private Bid                         bid = null;
    private JCoinchePlayer              generalBeginner = null;
    /**
     * GameThread Constructor
     * @param channelGroup
     * @param gameHandle
     */
    public                              GameThread(ChannelGroup channelGroup, GameHandle gameHandle) {
        this.channelGroup = channelGroup;
        this.gameHandle = gameHandle;
        this.messages = new ArrayList<>();
        this.teams = new ArrayList<JCoincheTeam>();
        this.cardGenerator = new CardGenerator();
    }

    /**
     * run() method for the game thread
     */
    @Override
    public void                         run() {
        this.initializeTeams();
        this.generalBeginner = this.allPlayers.get(0);
        this.bid = new Bid(teams, allPlayers, generalBeginner, cardGenerator);
        while (this.isRunning) {
            while(!this.checkScoreTeams()) {
                System.out.println("inside boucle jeu");
                try {
                Thread.sleep(500);

            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
/*            try {
                System.out.println(String.format(JCoincheConstants.log_game_thread_status, this.channelGroup.size()));
                if (this.messages.size() > 0) {
                    String lastMessage = this.messages.get(this.messages.size() - 1);
                    System.out.println(String.format(JCoincheConstants.log_message_found, lastMessage));
                    this.removeMessage(lastMessage);
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } */
            }
        }
    }

    private ArrayList<Channel>          channelGroupToArrayList() {
        ArrayList<Channel> channels = new ArrayList<>();

        for (Channel ch : this.channelGroup) {
            channels.add(ch);
        }
        return channels;
    }

    private void                        initializeTeams() {
        ArrayList<Channel>              n_channels = this.channelGroupToArrayList();

        this.allPlayers = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            JCoinchePlayer  player = new JCoinchePlayer(n_channels.get(i), i + 1);
            allPlayers.add(player);
        }

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
            JCoinchePlayer partner;
            p.getChannel().writeAndFlush(MessageForger.forgeGameStartMessage(
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

    private void                        broadcastGameStart() {

    }
    /**
     * stop the game thread
     * @return GameThread
     */
    public GameThread                   stopGame() {
        this.isRunning = false;
        this.messages.clear();
        return this;
    }

    /**
     * Add a message to the game thread queue
     * @param message
     * @return GameThread
     */
    public GameThread                   addMessage(String message) {
        this.messages.add(message);
        return this;
    }

    /**
     * Remove a message to the game thread queue
     * @param message
     * @return GameThread
     */
    public GameThread                   removeMessage(String message) {
        this.messages.remove(message);
        return this;
    }
}
