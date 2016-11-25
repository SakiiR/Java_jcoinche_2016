/**
 * Created by sakiir on 20/11/16.
 */
public class                JCoincheCard {
    public enum      Color {
        HEART,
        DIAMOND,
        CLUB,
        SPADE
    }

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


    public                  JCoincheCard(Color color, Id id) {
        this.color = color;
        this.id = id;
        this.value = 0;
    }

    public                  JCoincheCard(JCoincheCard other) {
        this.color = other.getColor();
        this.id = other.getId();
        this.player = other.getPlayer();
        this.value = other.getValue();
    }

    public                  JCoincheCard(Color color, Id id, JCoinchePlayer player) {
        this(color, id);
        this.setPlayer(player);
    }

    public int              getValue() { return value; }

    public void             setValue(int value) { this.value = value; }

    public Color            getColor() {
        return this.color;
    }

    public Id               getId() {
        return this.id;
    }

    public JCoinchePlayer   getPlayer() {
        return this.player;
    }

    public JCoincheCard     setPlayer(JCoinchePlayer player) {
        this.player = player;
        return this;
    }
}
