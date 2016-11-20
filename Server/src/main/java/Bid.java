import java.util.ArrayList;

/**
 * Created by anakin on 20/11/16.
 */
public class                            Bid {
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   allPlayers = null;
    private JCoinchePlayer              beginner = null;
    private JCoinchePlayer              bidBeginner = null;
    private CardGenerator               cardGenerator = null;

    public                              Bid(ArrayList<JCoincheTeam> teams,
                                            ArrayList<JCoinchePlayer> players,
                                            JCoinchePlayer beginner, CardGenerator cardGenerator) {
        this.teams = teams;
        this.allPlayers = players;
        this.beginner = beginner;
        this.cardGenerator = cardGenerator;
    }

    public void                         runBid()
    {
        boolean                         hasBid = false;

        while (!hasBid) {

        }
    }
}
