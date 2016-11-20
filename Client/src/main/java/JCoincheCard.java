/**
 * Created by sakiir on 20/11/16.
 */
public class                JCoincheCard {
    public enum             Color {
        HEART,
        DIAMOND,
        CLUB,
        SPADE
    }

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

    public                  JCoincheCard(Color color, Id id) {
        this.color = color;
        this.id = id;
    }

    public Color            getColor() {
        return this.color;
    }

    public Id               getId() {
        return this.id;
    }
}
