package me.riseremi.utils;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class Utils {

    public static int getInt(String str) {
        return Integer.parseInt(str);
    }

    public static int toBit(int num) {
        if (num > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
