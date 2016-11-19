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

    public static final JCoincheProtocol.JCoincheMessage.Builder    forgeGameStartMessage(String token, int playerId, int teamId, int partnerId) {
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
}
