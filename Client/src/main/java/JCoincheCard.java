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
     * @param color Color Id
     * @param id Card Id
     */
    public                  JCoincheCard(Color color, Id id) {
        this.color = color;
        this.id = id;
    }

    /**
     * Retrieving the card color
     *
     * @return Card Color
     */
    public Color            getColor() {
        return this.color;
    }

    /**
     * Retrieving the card id
     *
     * @return Card Id
     */
    public Id               getId() {
        return this.id;
    }

    /**
     * equals overloading to know if two cards are the same
     * by checking it Color and Id
     *
     * @param other The compared Card
     * @return true if equals, false if not
     */
    public boolean          equals(JCoincheCard other) {
        return (
                this.color == other.getColor() &&
                this.id == other.getId()
        );
    }
}
