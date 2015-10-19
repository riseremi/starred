package me.riseremi.core;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public final class Global {

    public final static int tileWidth = 32, tileHeight = 32;
    public final static int CHAT_HEIGHT = 100;
    public final static int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 600 + CHAT_HEIGHT;
    public final static int VIEWPORT_HEIGHT = 600;
    public final static int verticalTiles = 128, horizontalTiles = 128;
    public final static int mapWidth = tileWidth * horizontalTiles, mapHeight = tileHeight * verticalTiles;
    //высота и ширина области отрисовки в тайлах, чтобы не перерисовывать всю карту каждый тик
    public final static int paintWidth = WINDOW_WIDTH / tileWidth / 1, paintHeight = WINDOW_HEIGHT / tileHeight / 1;
    public static final int CENTER_Y = (Global.WINDOW_HEIGHT - Global.CHAT_HEIGHT) / Global.tileHeight / 2;
    public static final int CENTER_X = Global.WINDOW_WIDTH / Global.tileWidth / 2;
    public final static String pathToTheMap = "/res/new_map2.m";
    public final static String pathToTheTiles = "/res/new_tiles.png";
//    public final static String pathToTheTiles = "/res/pooshka_tiles.png";

    public static int translateX(int x) {
        final int blocksX = Core_v1.getInstance().getWorld().getBackgroundLayer().getBlocksX();
        return (x + blocksX);
    }

    public static int translateY(int y) {
        final int blocksY = Core_v1.getInstance().getWorld().getBackgroundLayer().getBlocksY();
        return (y + blocksY);
    }
}
