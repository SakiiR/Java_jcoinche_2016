import java.math.BigInteger;
import io.netty.channel.Channel;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by anakin on 19/11/16.
 */

/**
 * This class represent a player inside a game.
 *
 * @see JCoincheTeam
 * @see GameThread
 * @see GameHandle
 */
public class                                    JCoinchePlayer {
    private JCoincheTeam                        team;
    private Channel                             channel;
    private String                              token;
    private int                                 id;
    private JCoinchePlayer                      partner;
    private ArrayList<JCoincheCard>             cards;
    private JCoincheProtocol.JCoincheMessage    message = null;
    private GameThread                          gameThread = null;

    /**
     * Player constructor.
     *
     * @param channel Channel assigned to player.
     */
    public                                      JCoinchePlayer(Channel channel) {
        this.channel = channel;
        this.generateToken();
        this.cards = new ArrayList<>();
    }

    /**
     * Retrieve the game thread associated
     * to the player.
     *
     * @return the GameThread.
     * @see GameThread
     */
    public GameThread                           getGameThread() {
        return gameThread;
    }

    /**
     * Set the game thread.
     *
     * @param gameThread the GameThread to set.
     * @return JCoinchePlayer instance.
     */
    public JCoinchePlayer                       setGameThread(GameThread gameThread) {
        this.gameThread = gameThread;
        return this;
    }

    /**
     * Retrieve the team reference.
     *
     * @return JCoincheTeam
     * @see JCoincheTeam
     */
    public JCoincheTeam                         getTeam() {
        return this.team;
    }

    /**
     * Retrieve Token.
     *
     * @return the string token.
     */
    public String                               getToken() {
        return this.token;
    }

    /**
     * Set the token
     *
     * @param token the token string
     * @return JCoinchePlayer instance
     */
    public JCoinchePlayer                       setToken(String token) {
        this.token = token;
        return this;
    }

    /**
     * Set the associated team.
     *
     * @param team The team
     * @return JCoinchePlayer instance.
     */
    public JCoinchePlayer                       setTeam(JCoincheTeam team) {
        this.team = team;
        return this;
    }

    /**
     * Generate the token and store it.
     *
     * @return JCoinchePlayer instance.
     */
    private JCoinchePlayer                      generateToken() {
        this.token = new BigInteger(130, new SecureRandom()).toString(32);
        return this;
    }

    /**
     * Set the player id.
     *
     * @param id the id to set.
     * @return JCoinchePlayer instance.
     */
    public JCoinchePlayer                       setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * Retrive the player id.
     *
     * @return the player id
     */
    public int                                  getId() {
        return this.id;
    }

    /**
     * Retrieve partner id.
     *
     * @return JCoinchePlayer reference to partner.
     */
    public JCoinchePlayer                       getPartner() {
        return this.partner;
    }

    /**
     * Set the partner player.
     *
     * @param p the partner.
     * @return JCoinchePlayer instance.
     */
    public JCoinchePlayer                       setPartner(JCoinchePlayer p) {
        this.partner = p;
        return this;
    }

    /**
     * Retrieve the cards.
     *
     * @return an array of player cards.
     */
    public ArrayList<JCoincheCard>              getCards() {
        return this.cards;
    }

    /**
     * Display the cards to the input in green.
     */
    public void                                 dumpCards() {
        JCoincheUtils.logSuccess(JCoincheConstants.log_each_player_card_infos);
        for (JCoincheCard c : this.cards) {
            JCoincheUtils.logSuccess(JCoincheConstants.log_card_info, c.getColor().ordinal(), c.getId().ordinal());
        }
    }

    /**
     * Retrieve the lastMessage.
     *
     * @return the last message.
     */
    public JCoincheProtocol.JCoincheMessage     getMessage() {
        return message;
    }

    /**
     * Set the lastMessage.
     *
     * @param message the last message.
     * @return JCoinchePlayer instance.
     */
    public JCoinchePlayer                       setMessage(JCoincheProtocol.JCoincheMessage message) {
        this.message = message;
        return this;
    }

    /**
     * Retrieve the Player channel.
     *
     * @return The associated channel.
     */
    public Channel                              getChannel() {
        return this.channel;
    }

    /**
     * Set the associated channel.
     *
     * @param channel Channel to set.
     * @return JCoinchePlayer instance.
     */
    public JCoinchePlayer                       setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }
}
