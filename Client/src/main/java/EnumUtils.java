/**
 * Created by sakiir on 22/11/16.
 */
public class                            EnumUtils {
    public enum                         Trump {
        WA,
        AT
    }

    public static JCoincheCard.Color    getColorByIndex(int index) {
        return (JCoincheCard.Color.valueOf(JCoincheCard.Color.values()[index].name()));
    }

    public static JCoincheCard.Id       getIdByIndex(int index) {
        return (JCoincheCard.Id.valueOf(JCoincheCard.Id.values()[index].name()));
    }

    public static Trump                 getTrumpTypeByIndex(int index) {
        return (Trump.valueOf(Trump.values()[index].name()));
    }
}
