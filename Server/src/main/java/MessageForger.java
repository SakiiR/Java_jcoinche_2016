import java.util.ArrayList;

/**
 * Created by sakiir on 19/11/16.
 */
public class                                                        MessageForger {
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeWelcomeMessage(String message) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder().setType(
                        JCoincheProtocol
                                .JCoincheMessage
                                .Type
                                .WELCOME
                )
                .setWelcomeMessage(
                        JCoincheProtocol
                                .WelcomeMessage
                                .newBuilder()
                                .setMessage(message)
                ));
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeGameStartMessage(String token, int playerId, int teamId, int partnerId, String uniqueGameThreadId) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol
                        .JCoincheMessage
                        .Type
                        .GAME_START)
                .setGameStartMessage(JCoincheProtocol
                        .GameStartMessage
                        .newBuilder()
                        .setToken(token)
                        .setPartnerId(partnerId)
                        .setTeamId(teamId)
                        .setUniqueGameThreadId(uniqueGameThreadId)
                        .setPlayerId(playerId)));
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeGetCardsMessage(JCoinchePlayer player) {
        ArrayList<JCoincheCard> cards = player.getCards();
        JCoincheProtocol.JCoincheMessage.Builder builder = JCoincheProtocol.JCoincheMessage.newBuilder();
        JCoincheProtocol.GetCardsMessage.Builder cardsMessage = JCoincheProtocol.GetCardsMessage.newBuilder();

        for (JCoincheCard c : cards) {
            cardsMessage.addColors(c.getColor().ordinal())
                    .addIds(c.getId().ordinal());
        }

        builder.setType(JCoincheProtocol.JCoincheMessage.Type.GET_CARDS)
                .setGetCardsMessage(cardsMessage);
        return builder;
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeGetBidMessage(int value) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol
                        .JCoincheMessage
                        .Type.GET_BID)
                .setGetBidMessage(JCoincheProtocol
                        .GetBidMessage.newBuilder()
                        .setValue(value)));
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeError(String message) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.ERROR)
                .setErrorMessage(JCoincheProtocol
                        .ErrorMessage
                        .newBuilder()
                        .setMessage(message))
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendBidMessage(JCoincheBidInformations bidInformations, boolean bid, JCoinchePlayer player) {

        if (bid) {
            int trump;

            trump = bidInformations.getBidTrump().ordinal();
            return (JCoincheProtocol
                    .JCoincheMessage
                    .newBuilder().setType(JCoincheProtocol.JCoincheMessage.Type.SEND_BID)
                    .setSendBidMessage(JCoincheProtocol
                            .SendBidMessage
                            .newBuilder()
                            .setPlayerId(player.getId())
                            .setBid(true)
                            .setBidValue(bidInformations.getBidValue())
                            .setBidTrump(trump)));
        } else {
            return (JCoincheProtocol
                    .JCoincheMessage
                    .newBuilder().setType(JCoincheProtocol.JCoincheMessage.Type.SEND_BID)
                    .setSendBidMessage(JCoincheProtocol
                            .SendBidMessage
                            .newBuilder()
                            .setPlayerId(player.getId())
                            .setBid(false)));
        }
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeGetCoincheMessage() {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.GET_COINCHE)
                .setGetCoincheMessage(JCoincheProtocol
                        .GetCoincheMessage
                        .newBuilder())
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendCoincheMessage(int id) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_COINCHE)
                .setSendCoincheMessage(JCoincheProtocol
                        .SendCoincheMessage
                        .newBuilder()
                        .setPlayerId(id)));
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeGetSurcoincheMessage() {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.GET_SURCOINCHE)
                .setGetSurcoincheMessage(JCoincheProtocol
                        .GetSurcoincheMessage
                        .newBuilder())
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendSurcoincheMessage(int id) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_SURCOINCHE)
                .setSendSurcoincheMessage(JCoincheProtocol
                        .SendSurcoincheMessage
                        .newBuilder()
                        .setPlayerId(id))
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeGameStoppedMessage() {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.GAME_STOPPED)
                .setGameStoppedMessage(JCoincheProtocol
                        .GameStoppedMessage
                        .newBuilder())
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendBidInfoMessage(JCoincheBidInformations bidInfo) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_BID_INFO)
                .setSendBidInfoMessage(JCoincheProtocol
                        .SendBidInfoMessage
                        .newBuilder()
                        .setPlayerId(bidInfo.getBiddenPlayer().getId())
                        .setValue(bidInfo.getBidValue())
                        .setTrump(bidInfo.getBidTrump().ordinal()))
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeStartTrickMessage(int nb) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.START_TRICK)
                .setStartTrickMessage(JCoincheProtocol
                        .StartTrickMessage
                        .newBuilder()
                        .setTrickNumber(nb))
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeGetCardMessage() {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.GET_CARD)
                .setGetCardMessage(JCoincheProtocol
                        .GetCardMessage
                        .newBuilder())
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendCardMessage(int playerId, int cardId, int cardColor) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_CARD)
                .setSendCardMessage(JCoincheProtocol
                        .SendCardMessage
                        .newBuilder()
                        .setPlayerId(playerId)
                        .setCardId(cardId)
                        .setCardColor(cardColor))
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendWinTrickMessage(int playerId, int teamId, int score) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_WIN_TRICK)
                .setSendWinTrickMessage(JCoincheProtocol
                        .SendWinTrickMessage
                        .newBuilder()
                        .setPlayerId(playerId)
                        .setTeamId(teamId)
                        .setScore(score))
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendWinRoundMessage(int bidderTeamId, int bidderTeamRoundScore, int bidderTeamScore,
                                                                                             int otherTeamId, int otherTeamRoundScore, int otherTeamScore, String message) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_WIN_ROUND)
                .setSendWinRoundMessage(JCoincheProtocol
                        .SendWinRoundMessage
                        .newBuilder()
                        .setBidderTeamId(bidderTeamId)
                        .setBidderTeamRoundScore(bidderTeamRoundScore)
                        .setBidderTeamScore(bidderTeamScore)
                        .setOtherTeamId(otherTeamId)
                        .setOtherTeamRoundScore(otherTeamRoundScore)
                        .setOtherTeamScore(otherTeamScore)
                        .setMessage(message))
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeEndGameMessage(int winnerTeamId, int winnerScore, int looserTeamId, int looserScore) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.END_GAME)
                .setEndGameMessage(JCoincheProtocol
                        .EndGameMessage
                        .newBuilder()
                        .setWinnerTeamId(winnerTeamId)
                        .setWinnerScore(winnerScore)
                        .setLooserTeamId(looserTeamId)
                        .setLooserScore(looserScore))
        );
    }
}
