import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by sakiir on 20/11/16.
 */
public class                        CardGenerator {
    ArrayList<JCoincheCard>         cards;

    public                          CardGenerator() {
        this.cards = new ArrayList<>();
        this.generate32Cards();
    }

    private void                    generate32Cards() {
        System.out.println(JCoincheConstants.log_generating_cards);
        for (int i = 0 ; i < JCoincheCard.Color.values().length ; ++i) {
            for (int j = 0 ; i < JCoincheCard.Id.values().length ; ++j) {
                this.cards.add(
                        new JCoincheCard(
                                JCoincheCard.Color.valueOf(JCoincheCard.Color.values()[i].name()),
                                JCoincheCard.Id.valueOf(JCoincheCard.Id.values()[i].name())
                        ));
            }
        }
        System.out.println(JCoincheConstants.log_generated_cards);
    }
}
