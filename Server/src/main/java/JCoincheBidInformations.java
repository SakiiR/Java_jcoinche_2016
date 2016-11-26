/**
 * Created by anakin on 20/11/16.
 */

/**
 * This class is used to store information from
 * the game or the bid more specifically.
 *
 * @see GameThread
 */
public class                        JCoincheBidInformations {
    private int                     bidValue;
    private BidTrump                bidTrump = null;
    private JCoinchePlayer          biddenPlayer = null;
    private boolean                 coinche;
    private boolean                 surcoinche;

    /**
     * This enum is representing all the possibility of Trump
     * inside the doudoune coinché.
     */
    public enum                     BidTrump {
        HEART,
        DIAMOND,
        CLUB,
        SPADE,
        WT,
        AT
    }

    /**
     * JCoincheBidInformations Constructor.
     *
     * @see JCoincheBid
     */
    public                          JCoincheBidInformations() {
        this.coinche = false;
        this.surcoinche = false;
        this.bidValue = 0;
    }

    /**
     * Retrieve bid trump.
     *
     * @return a BidTrump enum element.
     */
    public BidTrump                  getBidTrump() {
        return bidTrump;
    }

    /**
     * Set the bid trump.
     *
     * @param bidType the bid trump
     * @return An instance of JCoincheBidInformations.
     */
    public JCoincheBidInformations  setBidTrump(BidTrump bidType) {
        this.bidTrump = bidType;
        return this;
    }

    /**
     * Retrieve the bidden player reference.
     *
     * @return The JCoinchePlayer ref.
     */
    public JCoinchePlayer           getBiddenPlayer() {
        return biddenPlayer;
    }

    /**
     * Set the biddenPlayer
     *
     * @param biddenPlayer the JCoinchePlayer to set as bidden player.
     * @return JCoincheBidInformations instance.
     */
    public JCoincheBidInformations  setBiddenPlayer(JCoinchePlayer biddenPlayer) {
        this.biddenPlayer = biddenPlayer;
        return this;
    }

    /**
     * is coinche ?
     *
     * @return true if coinche is true;
     */
    public boolean                  isCoinche() {
        return coinche;
    }

    /**
     * Set the coinche value.
     *
     * @param coinche coinche value
     * @return JCoincheBidInformations instance.
     */
    public JCoincheBidInformations  setCoinche(boolean coinche) {
        this.coinche = coinche;
        return this;
    }

    /**
     * Is Surcoinche ?
     *
     * @return a boolean surcoinche if surcoinche.
     */
    public boolean                  isSurcoinche() {
        return surcoinche;
    }

    /**
     * Set the surcoinche value.
     *
     * @param surcoinche the value to set as surcoinche.
     * @return JCoincheBidInformations instance
     */
    public JCoincheBidInformations  setSurcoinche(boolean surcoinche) {
        this.surcoinche = surcoinche;
        return this;
    }

    /**
     * Retrieve the bid value.
     *
     * @return a bid value.
     */
    public int                      getBidValue() {
        return bidValue;
    }

    /**
     * Set the bid value.
     *
     * @param bidValue the bid value.
     * @return JCoincheBidInformations instance.
     */
    public JCoincheBidInformations  setBidValue(int bidValue) {
        this.bidValue = bidValue;
        return this;
    }
}

