import java.util.ArrayList;

/**
 * Created by anakin on 24/11/16.
 */
public class                    JCoincheRound {
    JCoincheBidInformations     bidInformations = null;
    JCoinchePlayer              trickBeginner = null;
    JCoinchePlayer              beginner = null;
    ArrayList<JCoincheTeam>     teams = null;
    ArrayList<JCoinchePlayer>   players = null;

    public                  JCoincheRound(JCoincheBidInformations bidInfo, JCoinchePlayer beginner, ArrayList<JCoincheTeam> teams, ArrayList<JCoinchePlayer> players) {
        this.bidInformations = bidInfo;
        this.trickBeginner = beginner;
        this.beginner = beginner;
        this.teams = teams;
        this.players = players;
    }
}
