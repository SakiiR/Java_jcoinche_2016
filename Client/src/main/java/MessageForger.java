/**
 * Created by sakiir on 21/11/16.
 */

import io.netty.channel.Channel;

/**
 * This class only contains static method.
 * Theses methods are forging a JCoincheMessage Google Protocol Buffer
 * Object wich is given to the JCoincheUtils.writeAndFlush static methods.
 *
 * @see JCoincheUtils#writeAndFlush(Channel, Object)
 */
public class                                                        MessageForger {
    /**
     * Forge a SET_BID Message
     *
     * @param token Player token
     * @return JCoincheMessageBuilder
     */
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSetBidMessage(String token) {
        JCoincheProtocol.JCoincheMessage.Builder                    builder = JCoincheProtocol.JCoincheMessage.newBuilder();
        JCoincheProtocol.SetBidMessage.Builder                      bidBuilder = JCoincheProtocol.SetBidMessage.newBuilder();

        bidBuilder.setBid(false);
        return (builder.setToken(token)
                .setType(JCoincheProtocol
                        .JCoincheMessage
                        .Type
                        .SET_BID)
                .setSetBidMessage(bidBuilder));
    }

    /**
     * Forge a SET_BID Message with bidValue and trump
     *
     * @param token Player token
     * @param bidValue bidValue
     * @param trump trump
     * @return JCoincheMessageBuilder
     */
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSetBidMessage(String token,  int bidValue, int trump) {
        JCoincheProtocol.JCoincheMessage.Builder                    builder = JCoincheProtocol.JCoincheMessage.newBuilder();
        JCoincheProtocol.SetBidMessage.Builder                      bidBuilder = JCoincheProtocol.SetBidMessage.newBuilder();

        bidBuilder.setBid(true);
        bidBuilder.setBidValue(bidValue).setTrump(trump);
        return (builder.setToken(token)
                .setType(JCoincheProtocol
                        .JCoincheMessage
                        .Type
                        .SET_BID)
                .setSetBidMessage(bidBuilder));
    }

    /**
     * Forge a SET_COINCHE Message
     *
     * @param token Player token
     * @param coinche coinche or not ?
     * @return JCoincheMessageBuilder
     */
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSetCoincheMessage(String token, boolean coinche) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setToken(token)
                .setType(JCoincheProtocol
                        .JCoincheMessage
                        .Type
                        .SET_COINCHE)
                .setSetCoincheMessage(JCoincheProtocol
                        .SetCoincheMessage
                        .newBuilder()
                        .setCoinche(coinche))
        );
    }

    /**
     * Forge a SET_SURCOINCHE Message
     *
     * @param token token
     * @param surcoinche surcoinche
     * @return JCoincheMessageBuilder
     */
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSetSurcoincheMessage(String token, boolean surcoinche) {
        return (JCoincheProtocol
                .JCoincheMessage
                .newBuilder()
                .setToken(token)
                .setType(JCoincheProtocol
                        .JCoincheMessage
                        .Type
                        .SET_SURCOINCHE)
                .setSetSurcoincheMessage(JCoincheProtocol
                        .SetSurcoincheMessage
                        .newBuilder()
                        .setSurcoinche(surcoinche))
        );
    }

    /**
     * Forge a SET_CARD Message
     *
     * @param token Player token
     * @param c Card
     * @return JCoincheMessageBuilder
     */
    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeSetCardMessage(String token, JCoincheCard c) {
     return (JCoincheProtocol
             .JCoincheMessage
             .newBuilder()
             .setToken(token)
             .setType(JCoincheProtocol
                     .JCoincheMessage
                     .Type
                     .SET_CARD)
             .setSetCardMessage(JCoincheProtocol
                     .SetCardMessage
                     .newBuilder()
                     .setCardColor(c
                             .getColor()
                             .ordinal())
                     .setCardId(c
                             .getId()
                             .ordinal()))
     );
    }
}
