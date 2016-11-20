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

    private Color           color;
    private Id              id;
    private JCoinchePlayer  player;

    public                  JCoincheCard(Color color, Id id) {
        this.color = color;
        this.id = id;
    }

    public                  JCoincheCard(JCoincheCard other) {
        this.color = other.getColor();
        this.id = other.getId();
        this.player = other.getPlayer();
    }

    public                  JCoincheCard(Color color, Id id, JCoinchePlayer player) {
        this(color, id);
        this.setPlayer(player);
    }

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
