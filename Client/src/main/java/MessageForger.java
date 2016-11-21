/**
 * Created by sakiir on 21/11/16.
 */
public class                                                        MessageForger {
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
                        .setCoincheValue(coinche))
        );
    }
}
