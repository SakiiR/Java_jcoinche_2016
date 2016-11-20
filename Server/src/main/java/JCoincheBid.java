import java.util.ArrayList;

/**
 * Created by anakin on 20/11/16.
 */
public class                            JCoincheBid {
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   allPlayers = null;
    private JCoinchePlayer              beginner = null;
    private JCoinchePlayer              bidBeginner = null;
    private CardGenerator               cardGenerator = null;
    private JCoincheBidInformations     bidInformations = null;

    public                              JCoincheBid(ArrayList<JCoincheTeam> teams,
                                                    ArrayList<JCoinchePlayer> players,
                                                    JCoinchePlayer beginner,
                                                    CardGenerator cardGenerator) {
        this.teams = teams;
        this.allPlayers = players;
        this.beginner = beginner;
        this.cardGenerator = cardGenerator;
    }

    public void                         runBid()
    {
        boolean                         hasBid = false;

        this.bidInformations = new JCoincheBidInformations();
        while (!hasBid) {
            this.cardGenerator.spreadCards(allPlayers);
            //sendcard to all players

        }
    }
}
