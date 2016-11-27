import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

/**
 * Created by sakiir on 27/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class                        GameThreadTest extends TestCase {
    @Test
    public void                     gameThreadInitializeTeamsAndStopItTest() {
        ArrayList<JCoinchePlayer>   players = new ArrayList<>();
        GameThread                  gameThread;

        JCoincheUtils.logWarning("[TEST] gameThreadInitializeTeamsAndStopItTest");
        JCoincheUtils.logWarning("[TEST] Adding players");
        players.add(new JCoinchePlayer(null));
        players.add(new JCoinchePlayer(null));
        players.add(new JCoinchePlayer(null));
        players.add(new JCoinchePlayer(null));
        JCoincheUtils.logWarning("[TEST] Instanciating GameThread");
        gameThread = new GameThread(players, new GameHandle());
        Thread t = new Thread(gameThread);
        t.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameThread.setRunning(false);
        JCoincheUtils.logWarning("[TEST] Joining ..");
        try {
            t.join();
            JCoincheUtils.logWarning("[TEST] Definitly ending GameThread::run()");
        } catch (InterruptedException e ) {
            e.printStackTrace();
        }
        JCoincheUtils.logWarning("[TEST] Joined !");
        assert (gameThread.getAllPlayers().size() == 4); // maybe others tests ?
    }
}
