/**
 * Created by sakiir on 20/11/16.
 */

/**
 * This class is a Doudoune Coinché Card Representation
 *
 * @see PlayerInformations
 */
public class                JCoincheCard {
    /**
     * This enum is representing all the Card Colors
     *
     * @see PlayerInformations
     */
    public enum             Color {
        HEART,
        DIAMOND,
        CLUB,
        SPADE
    }

    /**
     * This enum is representing all the Card Id
     *
     * @see PlayerInformations
     */
    public enum             Id {
        ACE,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }

    private Color           color;
    private Id              id;

    /**
     * Card Construction
     *
     * @param color
     * @param id
     */
    public                  JCoincheCard(Color color, Id id) {
        this.color = color;
        this.id = id;
    }

    /**
     * Retreiving the card color
     *
     * @return
     */
    public Color            getColor() {
        return this.color;
    }

    /**
     * Retreiving the card id
     *
     * @return
     */
    public Id               getId() {
        return this.id;
    }

    /**
     * equals overloading to know if two cards are the same
     * by checking it Color && Id
     *
     * @param other
     * @return
     */
    public boolean          equals(JCoincheCard other) {
        return (
                this.color == other.getColor() &&
                this.id == other.getId()
        );
    }
}
