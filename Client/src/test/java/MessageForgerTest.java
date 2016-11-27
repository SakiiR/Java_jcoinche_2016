import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by sakiir on 27/11/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class                                        MessageForgerTest extends TestCase {
    @Test
    public void                                     forgeSetBidMessageTest() {
        JCoincheProtocol.JCoincheMessage.Builder    message = MessageForger.forgeSetBidMessage("prout");

        JCoincheUtils.logWarning("[TEST] forgeSetBidMessageTest");
        JCoincheUtils.logWarning("[TEST] Asserting ..");
        assert (message.getType() == JCoincheProtocol.JCoincheMessage.Type.SET_BID &&
                message.getToken() == "prout"
        );
    }

    @Test
    public void                                     forgeSetBidMessageTest2() {
        JCoincheProtocol.JCoincheMessage.Builder    message = MessageForger.forgeSetBidMessage("prout", 1337, EnumUtils.Trump.WT.ordinal());

        JCoincheUtils.logWarning("[TEST] forgeSetBidMessageTest2");
        JCoincheUtils.logWarning("[TEST] Asserting ..");
        assert (message.getType() == JCoincheProtocol.JCoincheMessage.Type.SET_BID &&
                message.getToken() == "prout" &&
                message.getSetBidMessage().getBidValue() == 1337 &&
                message.getSetBidMessage().getTrump() == EnumUtils.Trump.WT.ordinal()
        );
    }

    @Test
    public void                                     forgeSetCoincheMessageTest() {
        JCoincheProtocol.JCoincheMessage.Builder    message = MessageForger.forgeSetCoincheMessage("prout", true);

        JCoincheUtils.logWarning("[TEST] forgeSetCoincheMessageTest");
        JCoincheUtils.logWarning("[TEST] Asserting ..");
        assert (message.getType() == JCoincheProtocol.JCoincheMessage.Type.SET_COINCHE &&
                message.getToken() == "prout" &&
                message.getSetCoincheMessage().getCoinche() == true);
    }

    @Test
    public void                                     forgeSetSurcoincheMessageTest() {
        JCoincheProtocol.JCoincheMessage.Builder    message = MessageForger.forgeSetSurcoincheMessage("prout", true);

        JCoincheUtils.logWarning("[TEST] forgeSetSurcoincheMessageTest");
        JCoincheUtils.logWarning("[TEST] Asserting ..");
        assert (message.getType() == JCoincheProtocol.JCoincheMessage.Type.SET_SURCOINCHE &&
                message.getToken() == "prout" &&
                message.getSetSurcoincheMessage().getSurcoinche());
    }

    @Test
    public void                                     forgeSetCardMessageTest() {
        JCoincheCard                                c = new JCoincheCard(JCoincheCard.Color.SPADE, JCoincheCard.Id.ACE);
        JCoincheProtocol.JCoincheMessage.Builder    message = MessageForger.forgeSetCardMessage("prout", c);

        JCoincheUtils.logWarning("[TEST] forgeSetCardMessageTest");
        JCoincheUtils.logWarning("[TEST] Asserting ..");
        assert (message.getType() == JCoincheProtocol.JCoincheMessage.Type.SET_CARD &&
                message.getToken() == "prout" &&
                message.getSetCardMessage().getCardId() == c.getId().ordinal() &&
                message.getSetCardMessage().getCardColor() == c.getColor().ordinal()
        );
    }

}
