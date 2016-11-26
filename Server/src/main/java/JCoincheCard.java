/**
 * Created by sakiir on 20/11/16.
 */

/**
 * This class is a Doudoune Coinché Card Representation
 *
 * @see JCoincheBidInformations
 */
public class                JCoincheCard {
    /**
     * This enum is representing all the Card Colors
     */
    public enum      Color {
        HEART,
        DIAMOND,
        CLUB,
        SPADE
    }

    /**
     * This enum is representing all the Card Id
     */
    public enum      Id {
        ACE,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }

    public static final int    cardValueTrump[] = {11, 0, 0, 14, 10, 20, 3, 4};
    public static final int    cardValue[] = {11, 0, 0, 0, 10, 2, 3, 4};
    private Color               color;
    private Id                  id;
    private JCoinchePlayer      player;
    private int                 value;


    /**
     * Card default constructor.
     *
     * @param color the color to set.
     * @param id the id to set.
     */
    public                  JCoincheCard(Color color, Id id) {
        this.color = color;
        this.id = id;
        this.value = 0;
    }

    /**
     * The copy constructor.
     *
     * @param other The other card to get value from.
     */
    public                  JCoincheCard(JCoincheCard other) {
        this.color = other.getColor();
        this.id = other.getId();
        this.player = other.getPlayer();
        this.value = other.getValue();
    }

    /**
     * Second construcotor to set a player as owner.
     *
     * @param color The color to set.
     * @param id The id to set.
     * @param player the player to set as owner.
     */
    public                  JCoincheCard(Color color, Id id, JCoinchePlayer player) {
        this(color, id);
        this.setPlayer(player);
    }

    /**
     * Retrieve the card value.
     *
     * @return the card value.
     */
    public int              getValue() { return value; }

    /**
     * Set the value.
     *
     * @param value The card value.
     */
    public void             setValue(int value) { this.value = value; }

    /**
     * Retrieve the card color.
     *
     * @return the card color.
     */
    public Color            getColor() {
        return this.color;
    }

    /**
     * Retrieve the card id.
     *
     * @return the card id.
     */
    public Id               getId() {
        return this.id;
    }

    /**
     * Return the owner.
     *
     * @return The owner player.
     */
    public JCoinchePlayer   getPlayer() {
        return this.player;
    }

    /**
     * Set the layer as owner.
     *
     * @param player player to set as owner.
     * @return JCoincheCard instance.
     */
    public JCoincheCard     setPlayer(JCoinchePlayer player) {
        this.player = player;
        return this;
    }
}
