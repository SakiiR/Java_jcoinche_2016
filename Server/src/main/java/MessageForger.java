import java.util.ArrayList;

/**
 * Created by sakiir on 19/11/16.
 */
public class                                                        MessageForger {
    public static final JCoincheProtocol.JCoincheMessage.Builder forgeWelcomeMessage(String message) {
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

    public static final JCoincheProtocol.JCoincheMessage.Builder forgeGameStartMessage(String token, int playerId, int teamId, int partnerId) {
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
                        .setPlayerId(playerId)));
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder forgeGetCardsMessage(JCoinchePlayer player) {
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

    public static final JCoincheProtocol.JCoincheMessage.Builder forgeGetBidMessage(int value) {
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

    public static final JCoincheProtocol.JCoincheMessage.Builder forgeError(String message) {
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

    public static final JCoincheProtocol.JCoincheMessage.Builder forgeSendBidMessage(JCoincheBidInformations bidInformations, boolean bid, JCoinchePlayer player) {

        if (bid) {
            int trump;

            if (bidInformations.getBidType() != null) {
                trump = bidInformations.getBidType().ordinal();
            } else {
                trump = bidInformations.getBidTrump().ordinal();
            }
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

    public static final JCoincheProtocol.JCoincheMessage.Builder forgeGetCoincheMessage() {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.GET_COINCHE)
                .setGetCoincheMessage(JCoincheProtocol
                        .GetCoincheMessage
                        .newBuilder())
        );
    }

    public static final JCoincheProtocol.JCoincheMessage.Builder forgeSendCoincheMessage(int id) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setType(JCoincheProtocol.JCoincheMessage.Type.SEND_COINCHE)
                .setSendCoincheMessage(JCoincheProtocol
                        .SendCoincheMessage
                        .newBuilder()
                        .setPlayerId(id)));
    }
}
