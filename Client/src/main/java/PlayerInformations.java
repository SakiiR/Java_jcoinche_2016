/**
 * Created by sakiir on 19/11/16.
 */
public class                        PlayerInformations {
    private String                  token;
    private int                     teamId;
    private int                     playerId;

    public                          PlayerInformations() {  }

    public String                   getToken() {
        return this.token;
    }

    public PlayerInformations       setToken(String token){
        this.token = token;
        return this;
    }

    public int                      getTeamId() {
        return this.teamId;
    }

    public PlayerInformations       setTeamId(int teamId) {
        this.teamId = teamId;
        return this;
    }

    public int                      getPlayerId() {
        return this.playerId;
    }

    public PlayerInformations       setPlayerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

}
