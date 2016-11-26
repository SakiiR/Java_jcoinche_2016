import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by sakiir on 20/11/16.
 */

/**
 * This class is used to manage card for the players
 *
 * @see JCoincheCard
 * @see JCoinchePlayer
 */
public class                        CardGenerator {
    ArrayList<JCoincheCard>         cards;

    /**
     * CardGenerator Contructor
     */
    public                          CardGenerator() {
        this.cards = new ArrayList<>();
        this.generate32Cards();
    }

    /**
     * Return an array of cards
     *
     * @return an array of cards
     */
    public ArrayList<JCoincheCard>  getCards() {
        return this.cards;
    }

    /**
     * Assign 8 by 8 cards to players
     *
     * @param players The players array to asign card to
     */
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

    /**
     * This method is generating 32 unique
     * cards and store it in the private cards attr.
     *
     * @see CardGenerator#cards
     */
    private void                    generate32Cards() {
        JCoincheUtils.log(JCoincheConstants.log_generating_cards);
        for (int i = 0 ; i < JCoincheCard.Color.values().length ; ++i) {
            for (int j = 0 ; j < JCoincheCard.Id.values().length ; ++j) {
                JCoincheCard c = new JCoincheCard(
                        JCoincheCard.Color.valueOf(JCoincheCard.Color.values()[i].name()),
                        JCoincheCard.Id.valueOf(JCoincheCard.Id.values()[j].name())
                );
                this.cards.add(c);
            }
        }
        JCoincheUtils.log(JCoincheConstants.log_generated_cards);
    }
}
