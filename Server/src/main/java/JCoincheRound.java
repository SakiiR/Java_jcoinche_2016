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
    ArrayList<JCoincheTrick>    tricks = null;

    public                  JCoincheRound(JCoincheBidInformations bidInfo, JCoinchePlayer beginner, ArrayList<JCoincheTeam> teams, ArrayList<JCoinchePlayer> players) {
        this.bidInformations = bidInfo;
        this.trickBeginner = beginner;
        this.beginner = beginner;
        this.teams = teams;
        this.players = players;
        this.tricks = new ArrayList<>();
    }

    public void             run() {

        while (tricks.size() < 8) {
            this.tricks.add(new JCoincheTrick(trickBeginner, teams, players, bidInformations));
            this.sendTrickNbtoPlayers(this.tricks.size());
            this.tricks.get(this.tricks.size() - 1).run();
        }
        //fin du round, 8 plis accomplis => check du contrat application des points a la team gagnante plus broadcast
    }

    private void            sendTrickNbtoPlayers(int nb) {
        for (JCoinchePlayer p : this.players) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeStartTrickMessage(nb));
        }
    }
}
