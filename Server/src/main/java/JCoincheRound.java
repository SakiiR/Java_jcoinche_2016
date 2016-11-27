import java.util.ArrayList;

/**
 * Created by anakin on 24/11/16.
 */

/**
 * This class is used to create a new round of game.
 * A round is composed of several tricks.
 * It will generate the trickscore for each trick and
 * check if the bid has been respected.
 */
public class                            JCoincheRound {
    private JCoincheBidInformations     bidInformations = null;
    private JCoinchePlayer              trickBeginner = null;
    private JCoinchePlayer              beginner = null;
    private ArrayList<JCoincheTeam>     teams = null;
    private ArrayList<JCoinchePlayer>   players = null;
    private ArrayList<JCoincheTrick>    tricks = null;
    private GameThread                  gameThread = null;

    /**
     * The constructor of a JCoincheRound
     *
     * @param bidInfo the bidInfo from JCoincheBid
     * @param gameThread the GameThread from GameThread
     */
    public                      JCoincheRound(JCoincheBidInformations bidInfo, GameThread gameThread) {

        this.bidInformations = bidInfo;
        this.gameThread = gameThread;
        this.trickBeginner = this.gameThread.getGeneralBeginner();
        this.beginner = this.gameThread.getGeneralBeginner();
        this.teams = this.gameThread.getTeams();
        this.players = this.gameThread.getAllPlayers();
        this.tricks = new ArrayList<>();
    }

    /**
     * The method run will handle 8 tricks.
     * It will generate the score of the round,
     * checking if the bid has been respected.
     */
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
        JCoincheUtils.logInfo("[>] bidderteam tricscore = %d bidvalue = %d",
                this.bidInformations.getBiddenPlayer().getTeam().getTrickScore(), this.bidInformations.getBidValue());
        this.generateScoreTeams();
    }

    /**
     * This method will generate the score and
     * the winner team.
     */
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

    /**
     * This method broadcasts the round result
     * to all players.
     * It will send a SEND_WIN_ROUND Google Protocol
     * Buffer message to all players.
     *
     * @param bidderTeam the bidder team
     * @param otherTeam the other team
     * @param bidderTeamRoundScore the bidder score team
     * @param otherTeamRoundScore the other score team
     * @param message a specific message for SEND_WIN_ROUND
     */
    private void                sendWinRoundToPlayers(JCoincheTeam bidderTeam, JCoincheTeam otherTeam,
                                                      int bidderTeamRoundScore, int otherTeamRoundScore, String message) {
        for (JCoinchePlayer p : this.players) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeSendWinRoundMessage(bidderTeam.getId(),
                    bidderTeamRoundScore, bidderTeam.getScore(),
                    otherTeam.getId(), otherTeamRoundScore, otherTeam.getScore(), message));
        }
    }

    /**
     * Generate score round if the bid is a Capot.
     * It will evaluate is the bid has been made.
     *
     * @param bidderTeam the bidder team
     * @param otherTeam the other team
     */
    private void                generateScoreTeamsCapot(JCoincheTeam bidderTeam, JCoincheTeam otherTeam) {
        int                     tricksTeamBidder = 0;
        int                     bidderRoundScore = 0;
        int                     otherRoundScore = 0;

        for (JCoincheTrick t : this.tricks) {
            if (t.getTrickBeginner().getTeam() == bidderTeam) {
                tricksTeamBidder++;
            }
        }
        if (tricksTeamBidder == 8) {
            bidderRoundScore = 500;
            bidderTeam.setScore(bidderTeam.getScore() + bidderRoundScore);
            this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore,
                    otherRoundScore, "Team " + bidderTeam.getId() + " make his bid !");

        } else {
            otherRoundScore = 250 + otherTeam.getTrickScore();
            otherTeam.setScore(otherTeam.getScore() + 250 + otherTeam.getTrickScore());
            this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore,
                    otherRoundScore, "Team " + bidderTeam.getId() + " loose his bid !");
        }
    }

    /**
     * Generate score team if the bid has a Coinche
     * and/or a surcoinche.
     * It will evaluate if the bid has been made and
     * generate the score with a coinche or a surcoinche.
     *
     * @param bidderTeam the bidder team
     * @param otherTeam the other team
     */
    private void                generateScoreTeamsCoinche(JCoincheTeam bidderTeam, JCoincheTeam otherTeam) {
        int                     bidderRoundScore = 0;
        int                     otherRoundScore = 0;

        if (bidderTeam.getTrickScore() >= this.bidInformations.getBidValue()) {
            if (!this.bidInformations.isSurcoinche()) {
                bidderRoundScore = this.bidInformations.getBidValue() * 2 + bidderTeam.getTrickScore();
                otherRoundScore = otherTeam.getTrickScore();
                bidderTeam.setScore(bidderTeam.getScore() + bidderRoundScore);
                otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
                this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore,
                        otherRoundScore, "Team " + bidderTeam.getId() + " make his bid !");
            } else {
                bidderRoundScore = this.bidInformations.getBidValue() * 3 + bidderTeam.getTrickScore();
                otherRoundScore = otherTeam.getTrickScore();
                bidderTeam.setScore(bidderTeam.getScore() + bidderRoundScore);
                otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
                this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore,
                        otherRoundScore, "Team " + bidderTeam.getId() + " make his bid !");
            }
        } else {
            if (!this.bidInformations.isSurcoinche()) {
                otherRoundScore = this.bidInformations.getBidValue() * 2 + 160;
                otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
                this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore,
                        otherRoundScore, "Team " + bidderTeam.getId() + " loose his bid !");
            } else {
                otherRoundScore = this.bidInformations.getBidValue() * 3 + 160;
                otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
                this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore,
                        otherRoundScore, "Team " + bidderTeam.getId() + " loose his bid !");
            }
        }

    }

    /**
     * Generate the score team if the bid hasn't
     * coinche.
     * It will evaluate if the bid has been made.
     *
     * @param bidderTeam the bidder team
     * @param otherTeam the other team
     */
    private void                generateScoreTeamsNormal(JCoincheTeam bidderTeam, JCoincheTeam otherTeam) {
        int                     bidderRoundScore = 0;
        int                     otherRoundScore = 0;

        JCoincheUtils.logInfo("[>] inside generateScoreTeamsNormal");
        JCoincheUtils.logInfo("[>] bidderTeam trickScore = %d", bidderTeam.getTrickScore());
        if (bidderTeam.getTrickScore() >= this.bidInformations.getBidValue()) {
            JCoincheUtils.logInfo("[>] inside contrat réalisé");
            bidderRoundScore = this.bidInformations.getBidValue() + bidderTeam.getTrickScore();
            otherRoundScore = otherTeam.getTrickScore();
            bidderTeam.setScore(bidderTeam.getScore() + bidderRoundScore);
            otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
            this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore,
                    otherRoundScore, "Team " + bidderTeam.getId() + " make his bid !");
        } else {
            otherRoundScore = 160 + this.bidInformations.getBidValue();
            otherTeam.setScore(otherTeam.getScore() + otherRoundScore);
            this.sendWinRoundToPlayers(bidderTeam, otherTeam, bidderRoundScore,
                    otherRoundScore, "Team " + bidderTeam.getId() + " loose his bid !");
        }
    }

    /**
     * Broadcasts a SEND_TRICK Google Protocol Buffer Message
     * to all players.
     *
     * @param nb the trick number
     */
    private void                sendTrickNbtoPlayers(int nb) {
        for (JCoinchePlayer p : this.players) {
            JCoincheUtils.writeAndFlush(p.getChannel(), MessageForger.forgeStartTrickMessage(nb));
        }
    }
}
