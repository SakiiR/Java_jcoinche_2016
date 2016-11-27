import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by sakiir on 27/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class                ClientProcessTest extends TestCase {
    @Test
    public void             addMessageTest() {
        ClientProcess       clientProcess = new ClientProcess();
        Thread              t = new Thread(clientProcess);

        JCoincheUtils.logWarning("[TEST] addMessageTest");
        JCoincheProtocol.JCoincheMessage message = JCoincheProtocol.JCoincheMessage.newBuilder().setToken("loltoken").setType(JCoincheProtocol.JCoincheMessage.Type.GAME_START).getDefaultInstanceForType();
        JCoincheUtils.logWarning("[TEST] Starting Thread");
        t.start();
        JCoincheUtils.logWarning("[TEST] Adding message GAME_START");
        clientProcess.addMessage(message);
        JCoincheUtils.logWarning("[TEST] Stop the thread");
        clientProcess.stopThread();
        JCoincheUtils.logWarning("[TEST] Joining");
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JCoincheUtils.logWarning("[TEST] Joinded");
        JCoincheUtils.logWarning("[TEST] asserting .. %d == 0", clientProcess.getMessages().size());
        assert (clientProcess.getMessages().size() == 0);
    }
}
