package me.riseremi.map.world;

import java.util.Random;

/**
 *
 * @author remi
 */
public class CheckObstacles {

    private static Random rnd = new Random();

    public static boolean checkObstacle(World world, int x, int y) throws CloneNotSupportedException {
        return world.getNullLayer().getTile(x, y) == 0;
    }
}
