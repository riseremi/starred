package me.riseremi.items;

/**
 *
 * @author remi
 */
public final class TempNameSwitch {

    public static String switchName(int id) {
        switch (id) {
            default:
                return "Unknown item";
            case 0:
                return "Key";
            case 1:
                return "Potion";
            case 2:
                return "Shit";
            case 3:
                return "Letter";
            case 4:
                return "Ruby";
            case 5:
                return "Dolphin";
        }
    }
}
