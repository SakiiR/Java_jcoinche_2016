import io.netty.channel.Channel;

import java.util.ArrayList;

/**
 * Created by sakiir on 19/11/16.
 */
public class                        PlayerInformations {
    private String                  token;
    private int                     teamId;
    private int                     playerId;
    private Channel                 channel;
    private ArrayList<JCoincheCard> cards;
    private String                  bidValue = null;
    private String                  bidTrump = null;
    private JCoincheCard            lastCardPlayed = null;

    public Channel                  getChannel() {
        return channel;
    }

    public PlayerInformations       setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    public                          PlayerInformations() {
        this.cards = new ArrayList<>();
    }

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

    public ArrayList<JCoincheCard>  getCards() {
        return cards;
    }

    public PlayerInformations       setCards(ArrayList<JCoincheCard> cards) {
        this.cards = cards;
        return this;
    }

    public void                     dumpCard() {
        JCoincheUtils.logSuccess("[+] My Cards (%d) :", this.cards.size());
        for (JCoincheCard c : this.cards) {
            JCoincheUtils.logSuccess("->\t%s of %s", c.getId(), c.getColor());
        }
        JCoincheUtils.logSuccess("[+] My Cards End");
    }

    public void                     dumpCardWithIndex() {
        int                         i = 0;
        JCoincheUtils.logSuccess("[+] My Cards (%d) :", this.cards.size());
        for (JCoincheCard c : this.cards) {
            if (this.lastCardPlayed != null && c.getColor() == this.lastCardPlayed.getColor()) {
                JCoincheUtils.logWarning("->\t[%d] %s of %s", i, c.getId(), c.getColor());
            } else {
                JCoincheUtils.logSuccess("->\t[%d] %s of %s", i, c.getId(), c.getColor());
            }
            ++i;
        }
        JCoincheUtils.logSuccess("[+] My Cards End");
    }

    public String                   getBidValue() {
        return bidValue;
    }

    public PlayerInformations       setBidValue(String bidValue) {
        this.bidValue = bidValue;
        return this;
    }

    public String                   getBidTrump() {
        return bidTrump;
    }

    public PlayerInformations       setBidTrump(String bidTrump) {
        this.bidTrump = bidTrump;
        return this;
    }

    public JCoincheCard             getLastCardPlayed() {
        return lastCardPlayed;
    }

    public PlayerInformations       setLastCardPlayed(JCoincheCard lastCardPlayed) {
        this.lastCardPlayed = lastCardPlayed;
        return this;
    }
}
