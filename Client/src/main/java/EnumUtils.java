/**
 * Created by sakiir on 22/11/16.
 */

/**
 * This class is giving some utils relative to Cards
 *
 * @see JCoincheCard
 */
public class                            EnumUtils {

    /**
     * This Enum is defining the Trump possibilities
     */
    public enum                         Trump {
        HEART,
        DIAMOND,
        CLUB,
        SPADE,
        WT,
        AT
    }

    /**
     * Return the JCoincheCard color by it index in enum
     *
     * @param index
     * @return
     * @see JCoincheCard
     */
    public static JCoincheCard.Color    getColorByIndex(int index) {
        return (JCoincheCard.Color.valueOf(JCoincheCard.Color.values()[index].name()));
    }

    /**
     * Return the JCoincheCard id by it index in enum
     *
     * @param index
     * @return
     * @see JCoincheCard
     */
    public static JCoincheCard.Id       getIdByIndex(int index) {
        return (JCoincheCard.Id.valueOf(JCoincheCard.Id.values()[index].name()));
    }

    /**
     * Return the Trump by it index in enum
     *
     * @param index
     * @return
     * @see EnumUtils.Trump
     */
    public static Trump                 getTrumpTypeByIndex(int index) {
        return (Trump.valueOf(Trump.values()[index].name()));
    }
}
