package me.riseremi.utils;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class Shift {

    public static int ShiftNorth(int p, int distance) {
        return (p - distance);
    }

    public static int ShiftSouth(int p, int distance) {
        return (p + distance);
    }

    public static int ShiftEast(int p, int distance) {
        return (p + distance);
    }

    public static int ShiftWest(int p, int distance) {
        return (p - distance);
    }

}
