import java.lang.reflect.Array;
import java.security.SecureRandom;
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

    public void                     spreadCards(ArrayList<JCoinchePlayer> players) {
        SecureRandom                r = new SecureRandom();

        JCoincheUtils.log(JCoincheConstants.log_spreading_cards);
        for (JCoinchePlayer p : players) {
            p.getCards().clear();
        }
        for (JCoincheCard c : this.cards) {
            int randomPlayerIndex = r.nextInt(4);
            while (players.get(randomPlayerIndex).getCards().size() >= 8) {
                randomPlayerIndex = r.nextInt(4);
            }

            JCoincheCard newCard = new JCoincheCard(c);
            newCard.setPlayer(players.get(randomPlayerIndex));
            players.get(randomPlayerIndex).getCards().add(newCard);
        }
        JCoincheUtils.log(JCoincheConstants.log_spreaded_cards);
    }

    private void                    generate32Cards() {
        JCoincheUtils.log(JCoincheConstants.log_generating_cards);
        for (int i = 0 ; i < JCoincheCard.Color.values().length ; ++i) {
            for (int j = 0 ; j < JCoincheCard.Id.values().length ; ++j) {
                JCoincheCard c = new JCoincheCard(
                        JCoincheCard.Color.valueOf(JCoincheCard.Color.values()[i].name()),
                        JCoincheCard.Id.valueOf(JCoincheCard.Id.values()[j].name())
                );
                this.cards.add(c);
                JCoincheUtils.log(JCoincheConstants.log_card_info, c.getColor(), c.getId());
            }
        }
        JCoincheUtils.log(JCoincheConstants.log_generated_cards);
    }
}
