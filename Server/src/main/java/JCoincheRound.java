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
    GameThread                  gameThread = null;

    public                      JCoincheRound(JCoincheBidInformations bidInfo, JCoinchePlayer beginner, ArrayList<JCoincheTeam> teams, ArrayList<JCoinchePlayer> players, GameThread gameThread) {
        this.bidInformations = bidInfo;
        this.trickBeginner = beginner;
        this.beginner = beginner;
        this.teams = teams;
        this.players = players;
        this.tricks = new ArrayList<>();
        this.gameThread = gameThread;
    }

    public void                 run() {

        this.teams.get(0).setTrickScore(0);
        this.teams.get(1).setTrickScore(0);
        while (tricks.size() < 8 && this.gameThread.isRunning()) {
            this.tricks.add(new JCoincheTrick(trickBeginner, teams, players, bidInformations, this.gameThread));
            this.sendTrickNbtoPlayers(this.tricks.size());
            this.tricks.get(this.tricks.size() - 1).run();
            this.trickBeginner = this.tricks.get(this.tricks.size() - 1).getTrickBeginner();
        }
        if (!this.gameThread.isRunning()) return;
        JCoincheUtils.logInfo("bidderteam tricscore = %d bidvalue = %d", this.bidInformations.getBiddenPlayer().getTeam().getTrickScore(), this.bidInformations.getBidValue());
        this.generateScoreTeams();
        //fin du round, 8 plis accomplis => check du contrat application des points a la team gagnante plus broadcast
    }

    private void                generateScoreTeams() {
        JCoincheTeam            bidderTeam = this.bidInformations.getBiddenPlayer().getTeam();
        JCoincheTeam            otherTeam;

        if (bidderTeam.getId() == 1) {
            otherTeam = this.teams.get(1);
        } else {
            otherTeam = this.teams.get(0);
        }
        if (this.bidInformations.getBidValue() < 170) {
            if (this.bidInformations.isCoinche()) {
                this.generateScoreTeamsCoinche(bidderTeam, otherTeam);
            } else {
                this.generateScoreTeamsNormal(bidderTeam, otherTeam);
            }
        } else {
            this.generateScoreTeamsCapot(bidderTeam, otherTeam);
        }
    }

    private void                sendWinRoundToPlayers(JCoincheTeam bidderTeam, JCoincheTeam otherTeam, int bidderTeamRoundScore, int otherTeamRoundScore, String message) {
        for (JCoinchePlayer p : this.players) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendWinRoundMessage(bidderTeam.getId(), bidderTeamRoundScore, bidderTeam.getScore(),
                    otherTeam.getId(), otherTeamRoundScore, otherTeam.getScore(), message));
        }
    }

    private void                generateScoreTeamsCapot(JCoincheTeam bidderTeam, JCoincheTeam otherTeam) {
        int                     tricksTeamBidder = 0;
        int                     bidderRoundScore = 0;
        int                     otherRoundScore = 0;

        for (JCoincheTrick t : this.tricks) {
            if (t.getTrickBeginner().getTeam() == bidderTeam) {
                tricksTeamBidder++;
            }
        }
        if (tricksTeamBidder == 8) { // si bidderTeam a annoncé et réalisé son capot ?
            bidderRoundScore = 500;
            bidderTeam.setScore(bidderTeam.getScore() + bidderRoundScore);
            this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore, otherRoundScore, "Team " + bidderTeam.getId() + " make his bid !");

        } else { //si capot annoncé chuté
            otherRoundScore = 250 + otherTeam.getTrickScore();
            otherTeam.setScore(otherTeam.getScore() + 250 + otherTeam.getTrickScore());
            this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore, otherRoundScore, "Team " + bidderTeam.getId() + " loose his bid !");
        }
    }

    private void                generateScoreTeamsCoinche(JCoincheTeam bidderTeam, JCoincheTeam otherTeam) {
        int                     bidderRoundScore = 0;
        int                     otherRoundScore = 0;

        if (bidderTeam.getTrickScore() >= this.bidInformations.getBidValue()) { //si contrat réalisé
            if (!this.bidInformations.isSurcoinche()) { //si juste coinche
                bidderRoundScore = this.bidInformations.getBidValue() * 2 + bidderTeam.getTrickScore();
                otherRoundScore = otherTeam.getTrickScore();
                bidderTeam.setScore(bidderTeam.getScore() + bidderRoundScore);
                otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
                this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore, otherRoundScore, "Team " + bidderTeam.getId() + " make his bid !");
            } else { //si surcoinche
                bidderRoundScore = this.bidInformations.getBidValue() * 3 + bidderTeam.getTrickScore();
                otherRoundScore = otherTeam.getTrickScore();
                bidderTeam.setScore(bidderTeam.getScore() + bidderRoundScore);
                otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
                this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore, otherRoundScore, "Team " + bidderTeam.getId() + " make his bid !");
            }
        } else { // si contrat pas réalisé
            if (!this.bidInformations.isSurcoinche()) { //si juste coinche
                otherRoundScore = this.bidInformations.getBidValue() * 2 + 160;
                otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
                this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore, otherRoundScore, "Team " + bidderTeam.getId() + " loose his bid !");
            } else { // si surcoinche
                otherRoundScore = this.bidInformations.getBidValue() * 3 + 160;
                otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
                this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore, otherRoundScore, "Team " + bidderTeam.getId() + " loose his bid !");
            }
        }

    }

    private void                generateScoreTeamsNormal(JCoincheTeam bidderTeam, JCoincheTeam otherTeam) {
        int                     bidderRoundScore = 0;
        int                     otherRoundScore = 0;

        JCoincheUtils.logInfo("inside generateScoreTeamsNormal");
        JCoincheUtils.logInfo("bidderTeam trickScore = %d", bidderTeam.getTrickScore());
        if (bidderTeam.getTrickScore() >= this.bidInformations.getBidValue()) { //si contrat réalisé sans coinche / surcoinche
            JCoincheUtils.logInfo("inside contrat réalisé");
            bidderRoundScore = this.bidInformations.getBidValue() + bidderTeam.getTrickScore();
            otherRoundScore = otherTeam.getTrickScore();
            bidderTeam.setScore(bidderTeam.getScore() + bidderRoundScore);
            otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
            this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore, otherRoundScore, "Team " + bidderTeam.getId() + " make his bid !");
        } else { //si contrat pas rempli
            otherRoundScore = 160 + this.bidInformations.getBidValue();
            otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
            this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore, otherRoundScore, "Team " + bidderTeam.getId() + " loose his bid !");
        }
    }

    private void                sendTrickNbtoPlayers(int nb) {
        for (JCoinchePlayer p : this.players) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeStartTrickMessage(nb));
        }
    }
}
