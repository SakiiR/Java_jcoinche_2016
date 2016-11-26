import io.netty.channel.Channel;
import java.util.ArrayList;

/**
 * Created by sakiir on 19/11/16.
 */

/**
 * This class only contains static method.
 * Theses methods are forging a JCoincheMessage Google Protocol Buffer with specified type
 * Object which is given to the JCoincheUtils.writeAndFlush static methods.
 *
 * @see JCoincheUtils#writeAndFlush(Channel, Object)
 */
public class                                                        MessageForger {
    /**
     * Forge a WELCOME Message
     *
     * @param message Message
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a GAME_START Message.
     *
     * @param token The player token.
     * @param playerId The player id.
     * @param teamId The player's team id.
     * @param partnerId The player's partner player id.
     * @param uniqueGameThreadId The GameThread unique id.
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a GET_CARDS Message
     *
     * @param player The player where the cards are.
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge GET_BID Message
     *
     * @param value bid value
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge an ERROR Message
     *
     * @param message comment message
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge SEND_BID Message.
     *
     * @param bidInformations Bid Information.
     * @param bid is bid as boolean.
     * @param player The bidden player.
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge GET_COINCHE Message.
     *
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a SEND_COINCHE Message.
     *
     * @param playerId The player Id who coinche.
     * @return JCoincheMessageBuilder
     */
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendCoincheMessage(int playerId) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_COINCHE)
                .setSendCoincheMessage(JCoincheProtocol
                        .SendCoincheMessage
                        .newBuilder()
                        .setPlayerId(playerId)));
    }

    /**
     * Forge GET_SURCOINCHE Message.
     *
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a SEND_SURCOINCHE Message.
     *
     * @param playerId The player id who surcoinche.
     * @return JCoincheMessageBuilder
     */
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSendSurcoincheMessage(int playerId) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_SURCOINCHE)
                .setSendSurcoincheMessage(JCoincheProtocol
                        .SendSurcoincheMessage
                        .newBuilder()
                        .setPlayerId(playerId))
        );
    }

    /**
     * Forge a GAME_STOPPED Message.
     *
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a SEND_BID_INFO Message.
     *
     * @param bidInfo The bid information to send.
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a START_TRICK Message.
     *
     * @param trickNumber The Trick number.
     * @return JCoincheMessageBuilder
     */
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeStartTrickMessage(int trickNumber) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.START_TRICK)
                .setStartTrickMessage(JCoincheProtocol
                        .StartTrickMessage
                        .newBuilder()
                        .setTrickNumber(trickNumber))
        );
    }

    /**
     * Forge a GET_CARD Message.
     *
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a SEND_CARD Message.
     *
     * @param playerId Player id
     * @param cardId Card id
     * @param cardColor Card color
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a SEND_WIN_TRICK Message.
     *
     * @param playerId Player id
     * @param teamId team Id
     * @param score Score as int
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge a SEND_WIN_ROUND Message.
     *
     * @param bidderTeamId Bidder Team Id
     * @param bidderTeamRoundScore Bidder Team Score For Round
     * @param bidderTeamScore Bidder Team Score Total
     * @param otherTeamId Other Team Id
     * @param otherTeamRoundScore Other Team Score For Round
     * @param otherTeamScore Other Team Score Total
     * @param message Optional message
     * @return JCoincheMessageBuilder
     */
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

    /**
     * Forge an END_GAME Message.
     *
     * @param winnerTeamId Winner team id.
     * @param winnerScore Winner total score.
     * @param looserTeamId Looser team id.
     * @param looserScore Looser total score.
     * @return JCoincheMessageBuilder
     */
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
