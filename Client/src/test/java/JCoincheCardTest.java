import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by sakiir on 27/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class                        JCoincheCardTest extends TestCase {
    @Test
    public void                     colorTest() {
        JCoincheCard                c = new JCoincheCard(JCoincheCard.Color.CLUB, JCoincheCard.Id.JACK);

        JCoincheUtils.logWarning("[TEST] colorTest");
        JCoincheUtils.logWarning("[TEST] asserting %s == CLUB", c.getColor());
        assert (c.getColor() == JCoincheCard.Color.CLUB);
    }

    @Test
    public void                     idTest() {
        JCoincheCard                c = new JCoincheCard(JCoincheCard.Color.CLUB, JCoincheCard.Id.JACK);

        JCoincheUtils.logWarning("[TEST] idTest");
        JCoincheUtils.logWarning("[TEST] asserting %s == CLUB", c.getId());
        assert (c.getId() == JCoincheCard.Id.JACK);
    }

    @Test
    public void                     equalsTest() {
        JCoincheCard                c1 = new JCoincheCard(JCoincheCard.Color.DIAMOND, JCoincheCard.Id.EIGHT);
        JCoincheCard                c2 = new JCoincheCard(JCoincheCard.Color.DIAMOND, JCoincheCard.Id.EIGHT);

        JCoincheUtils.logWarning("[TEST] equalsTest");
        JCoincheUtils.logWarning("[TEST] Asserting ..");
        assert (c1.equals(c2));
    }

    @Test
    public void                     nonEqualsTest() {
        JCoincheCard                c1 = new JCoincheCard(JCoincheCard.Color.DIAMOND, JCoincheCard.Id.EIGHT);
        JCoincheCard                c2 = new JCoincheCard(JCoincheCard.Color.SPADE, JCoincheCard.Id.NINE);

        JCoincheUtils.logWarning("[TEST] nonEqualsTest");
        JCoincheUtils.logWarning("[TEST] Asserting ..");
        assert (!c1.equals(c2));
    }
}
