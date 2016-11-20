/**
 * Created by anakin on 20/11/16.
 */
public class                        JCoincheBidInformations {
    private int                     bidValue;
    private JCoincheCard.Color      bidTrump = null;
    private BidType                 bidType = null;
    private JCoinchePlayer          biddenPlayer = null;
    private boolean                 coinche;
    private boolean                 surcoinche;

    public enum                     BidType {
        WT,
        AT
    }

    public                          JCoincheBidInformations() {
        this.coinche = false;
        this.surcoinche = false;
        this.bidValue = 0;

    }

    public JCoincheCard.Color       getBidTrump() {
        return bidTrump;
    }

    public JCoincheBidInformations  setBidTrump(JCoincheCard.Color bidTrump) {
        this.bidTrump = bidTrump;
        return this;
    }

    public BidType                  getBidType() {
        return bidType;
    }

    public JCoincheBidInformations  setBidType(BidType bidType) {
        this.bidType = bidType;
        return this;
    }

    public JCoinchePlayer           getBiddenPlayer() {
        return biddenPlayer;
    }

    public JCoincheBidInformations  setBiddenPlayer(JCoinchePlayer biddenPlayer) {
        this.biddenPlayer = biddenPlayer;
        return this;
    }

    public boolean                  isCoinche() {
        return coinche;
    }

    public JCoincheBidInformations  setCoinche(boolean coinche) {
        this.coinche = coinche;
        return this;
    }

    public boolean                  isSurcoinche() {
        return surcoinche;
    }

    public JCoincheBidInformations  setSurcoinche(boolean surcoinche) {
        this.surcoinche = surcoinche;
        return this;
    }

    public int                      getBidValue() {
        return bidValue;
    }

    public JCoincheBidInformations  setBidValue(int bidValue) {
        this.bidValue = bidValue;
        return this;
    }
}

