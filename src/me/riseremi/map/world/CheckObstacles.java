package me.riseremi.map.world;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class CheckObstacles {

    public static boolean checkObstacle(World world, int x, int y) throws CloneNotSupportedException {
        return world.getObstaclesLayer().getTile(x, y) == 0;
    }
}
