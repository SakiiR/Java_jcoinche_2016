import io.netty.channel.Channel;

import java.util.ArrayList;

/**
 * Created by sakiir on 19/11/16.
 */

/**
 * This class is containing all the Player informations.
 *
 * @see MessageHandler#handleGameStartMessage(JCoincheProtocol.GameStartMessage)
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

    /**
     * PlayerInformation constructor
     *
     * @see MessageHandler#handleGameStartMessage(JCoincheProtocol.GameStartMessage)
     */
    public                          PlayerInformations() {
        this.cards = new ArrayList<>();
    }

    /**
     * Retrieve netty Channel object
     *
     * @return
     */
    public Channel                  getChannel() {
        return channel;
    }

    /**
     * Set the netty Channel object
     *
     * @param channel
     * @return
     */
    public PlayerInformations       setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Retreive the player token which
     * is used to authenticate to the server.
     *
     * @return
     */
    public String                   getToken() {
        return this.token;
    }

    /**
     * Set the player token
     *
     * @param token
     * @return
     */
    public PlayerInformations       setToken(String token){
        this.token = token;
        return this;
    }

    /**
     * Retrieve the player's team id
     *
     * @return
     */
    public int                      getTeamId() {
        return this.teamId;
    }

    /**
     * Set the team id of the client
     *
     * @param teamId
     * @return
     */
    public PlayerInformations       setTeamId(int teamId) {
        this.teamId = teamId;
        return this;
    }

    /**
     * Retrieve Player Id
     *
     * @return
     */
    public int                      getPlayerId() {
        return this.playerId;
    }

    /**
     * Set de Player Id
     *
     * @param playerId
     * @return
     */
    public PlayerInformations       setPlayerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

    /**
     * Retrieve the Cards Array
     *
     * @return
     * @see ArrayList
     */
    public ArrayList<JCoincheCard>  getCards() {
        return cards;
    }

    /**
     * Set the player cards array
     *
     * @param cards
     * @return
     * @see ArrayList
     */
    public PlayerInformations       setCards(ArrayList<JCoincheCard> cards) {
        this.cards = cards;
        return this;
    }

    /**
     * Display the list of the card.
     */
    public void                     dumpCard() {
        JCoincheUtils.logSuccess("[+] My Cards (%d) :", this.cards.size());
        for (JCoincheCard c : this.cards) {
            JCoincheUtils.logSuccess("->\t%s of %s", c.getId(), c.getColor());
        }
        JCoincheUtils.logSuccess("[+] My Cards End");
    }

    /**
     * Display the list of the card
     * with showing index to ask user
     * a response.
     */
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

    /**
     * Retrieve the player bid value
     *
     * @return
     */
    public String                   getBidValue() {
        return bidValue;
    }

    /**
     * Set the player bid Value
     *
     * @param bidValue
     * @return
     */
    public PlayerInformations       setBidValue(String bidValue) {
        this.bidValue = bidValue;
        return this;
    }

    /**
     * Retrieve the Player Bid Trump
     *
     * @return
     */
    public String                   getBidTrump() {
        return bidTrump;
    }

    /**
     * Set the Player Bid Trump
     *
     * @param bidTrump
     * @return
     */
    public PlayerInformations       setBidTrump(String bidTrump) {
        this.bidTrump = bidTrump;
        return this;
    }

    /**
     * Set the player last played card
     *
     * @return
     */
    public JCoincheCard             getLastCardPlayed() {
        return lastCardPlayed;
    }

    /**
     * Set the player last played card
     *
     * @param lastCardPlayed
     * @return
     */
    public PlayerInformations       setLastCardPlayed(JCoincheCard lastCardPlayed) {
        this.lastCardPlayed = lastCardPlayed;
        return this;
    }
}
