import java.util.ArrayList;

/**
 * Created by anakin on 19/11/16.
 */

/**
 * This class represent a team inside the doudoune coinché.
 *
 * @see GameThread
 * @see JCoinchePlayer
 */
public class                                JCoincheTeam {

    private ArrayList<JCoinchePlayer>       players = null;
    private int                             score;
    private int                             trickScore;
    private int                             teamId;

    /**
     * JCoincheTeam Constructor.
     *
     * @param players The player array to create the team.
     * @param teamId The team id.
     */
    public                                  JCoincheTeam (ArrayList<JCoinchePlayer> players, int teamId) {
        this.players = players;
        this.players.get(0).setTeam(this).setPartner(this.players.get(1));
        this.players.get(1).setTeam(this).setPartner(this.players.get(0));
        this.score = 0;
        this.trickScore = 0;
        this.teamId = teamId;
    }

    /**
     * Retrieve team id
     *
     * @return team id
     */
    public int                              getId() {
        return this.teamId;
    }

    /**
     * Retrieve score
     *
     * @return score as int
     */
    public int                              getScore() { return this.score; }

    /**
     * Retrieve the trick score.
     *
     * @return trick score as int.
     */
    public int                              getTrickScore() { return this.trickScore; }

    /**
     * Retrieve team players array.
     *
     * @return team players array.
     */
    public ArrayList<JCoinchePlayer>        getPlayers() {
        return players;
    }

    /**
     * Set the team score.
     *
     * @param score score as int.
     */
    public void                             setScore(int score) {
        this.score = score;
    }

    /**
     * Set the trick score.
     *
     * @param trickScore trick score as int.
     */
    public void                             setTrickScore(int trickScore) {
        this.trickScore = trickScore;
    }
}
