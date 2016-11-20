import java.math.BigInteger;
import io.netty.channel.Channel;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by anakin on 19/11/16.
 */
public class                                    JCoinchePlayer {
    private JCoincheTeam                        team;
    private Channel                             channel;
    private String                              token;
    private int                                 playerId;
    private JCoinchePlayer                      partner;
    private ArrayList<JCoincheCard>             cards;

    public                                      JCoinchePlayer(Channel channel, int playerId) {
        this.channel = channel;
        this.generateToken();
        this.playerId = playerId;
        this.cards = new ArrayList<>();
    }

    public JCoincheTeam                         getTeam() {
        return this.team;
    }

    public String                               getToken() {
        return this.token;
    }

    public JCoinchePlayer                       setTeam(JCoincheTeam team) {
        this.team = team;
        return this;
    }

    private JCoinchePlayer                      generateToken() {
        this.token = new BigInteger(130, new SecureRandom()).toString(32);
        return this;
    }

    public int                                  getId() {
        return this.playerId;
    }

    public JCoinchePlayer                       getPartner() {
        return this.partner;
    }

    public JCoinchePlayer                       setPartner(JCoinchePlayer p) {
        this.partner = p;
        return this;
    }

    public ArrayList<JCoincheCard>              getCards() {
        return this.cards;
    }

    public Channel              getChannel() {
        return this.channel;
    }
}
