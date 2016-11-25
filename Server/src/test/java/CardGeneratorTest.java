import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;

/**
 * Created by sakiir on 25/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class                                CardGeneratorTest extends TestCase{
    @Test
    public void                             generateTestSize() throws Exception{
        CardGenerator       cG = new CardGenerator();

        assertEquals(cG.getCards().size(), 32);
    }

    @Test
    public void                             generateTestUnique() throws Exception {
        CardGenerator       cG = new CardGenerator();
        boolean             equals = false;

        for (int i = 0 ; i < cG.getCards().size() ; ++i) {
            for (int j = 0 ; j < cG.getCards().size() ; ++j) {
                if (i != j && cG.getCards().get(i) == cG.getCards().get(j)) {
                    equals = true;
                }
            }
        }
        assert (!equals);
    }

    @Test
    public void                             spreadCardsSizeTest() throws Exception {
        ArrayList<JCoinchePlayer>           players = new ArrayList<>();
        CardGenerator                       cG = new CardGenerator();
        boolean                             goodSize = true;

        for (int i = 0 ; i < 4 ; ++i) {
            JCoinchePlayer p = new JCoinchePlayer(null);
            p.setId(i + 1);
            players.add(p);
        }
        cG.spreadCards(players);
        for (JCoinchePlayer p : players) {
            if (p.getCards().size() != 8) {
                goodSize = false;
            }
        }
        assert (goodSize);
    }

    @Test
    public void                             spreadCardsUniqueTest() throws Exception {
        ArrayList<JCoinchePlayer>           players = new ArrayList<>();
        CardGenerator                       cG = new CardGenerator();
        boolean                             isUnique = true;

        for (int i = 0 ; i < 4 ; ++i) {
            JCoinchePlayer p = new JCoinchePlayer(null);
            p.setId(i + 1);
            players.add(p);
        }
        cG.spreadCards(players);
        for (int i = 0 ; i < players.size() ; ++i) {
            for (int j = 0 ; j < players.size() ; ++j) {
                if (i != j && players.get(i).getCards() == players.get(j).getCards()) {
                    isUnique = false;
                    break;
                }
            }
        }
        assert (isUnique);
    }
}
