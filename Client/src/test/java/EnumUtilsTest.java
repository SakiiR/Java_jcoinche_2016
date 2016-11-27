import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by sakiir on 27/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class                EnumUtilsTest extends TestCase {
    @Test
    public void             getColorByIndexTest() {
        JCoincheUtils.logWarning("[TEST] getColorByIndexTest");
        JCoincheUtils.logWarning("[TEST] ASSERT %s == SPADE", EnumUtils.getColorByIndex(3));
        assert (EnumUtils.getColorByIndex(3) == JCoincheCard.Color.SPADE);
    }

    @Test
    public void             getIdByIndexTest() {
        JCoincheUtils.logWarning("[TEST] getIdByIndexTest");
        JCoincheUtils.logWarning("[TEST] ASSERT %s == TEN", EnumUtils.getIdByIndex(4));
        assert (EnumUtils.getIdByIndex(4) == JCoincheCard.Id.TEN);
    }

    @Test
    public void             getTrumpTypeByIndex() {
        JCoincheUtils.logWarning("[TEST] getTrumpTypeByIndex");
        JCoincheUtils.logWarning("[TEST] ASSERT %s == WT", EnumUtils.getTrumpTypeByIndex(4));
        assert (EnumUtils.getTrumpTypeByIndex(4) == EnumUtils.Trump.WT);
    }
}
