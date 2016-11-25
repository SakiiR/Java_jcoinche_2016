import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by sakiir on 25/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class                    GameHandleTest extends TestCase {
    @Test
    public void             addPlayerSizeTest() throws Exception {
        GameHandle          gH = new GameHandle();

        JCoincheUtils.logInfo("[TEST] addPlayerSize Test");
        for (int i = 0 ; i < 15 ; ++i) {
            gH.addPlayer(null);
        }
        assert (gH.getPlayers().size() == 15);
    }

    @Test
    public void             removeInnactiveChannelsTest() throws Exception {
        GameHandle          gH = new GameHandle();

        JCoincheUtils.logInfo("[TEST] InnactiveChannl Test");
        for (int i = 0 ; i < 15 ; ++i) {
            gH.addPlayer(null);
        }
        gH.removeInnactiveChannels();
        assert(gH.getPlayers().size() == 0);
    }
}