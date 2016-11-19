import java.util.ArrayList;

/**
 * Created by anakin on 19/11/16.
 */
public class                                JCoincheTeam {

    private ArrayList<JCoinchePlayer>       players = null;
    private int                             score;
    private int                             trickScore;
    private int                             teamId;

    public static int                       gTeamId = 1;

    public                                  JCoincheTeam (ArrayList<JCoinchePlayer> players) {
        this.players = players;
        this.players.get(0).setTeam(this).setPartner(this.players.get(1));
        this.players.get(1).setTeam(this).setPartner(this.players.get(0));
        this.score = 0;
        this.trickScore = 0;
        this.teamId = JCoincheTeam.gTeamId++;
    }

    public int                              getId() {
        return this.teamId;
    }
}