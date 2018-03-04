package me.riseremi.map.world;

import me.riseremi.core.Global;
import me.riseremi.map.layer.TiledLayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public final class World {

    private TiledLayer backgroundLayer;
    private TiledLayer decorationsLayer;
    private TiledLayer obstaclesLayer;

    public World(int tileWidth, int tileHeight, int width, int height) {
        try {
            obstaclesLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
            backgroundLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
            decorationsLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
        } catch (IOException ex) {
        }
    }

    public void draw(Graphics g) {
        backgroundLayer.paintLayer(g);
        decorationsLayer.paintLayer(g);
    }

    @Deprecated
    public int getX() {
        return backgroundLayer.getBlocksX();
    }

    @Deprecated
    public int getY() {
        return backgroundLayer.getBlocksY();
    }

    public TiledLayer getBackgroundLayer() {
        return this.backgroundLayer;
    }

    public TiledLayer getDecorationsLayer() {
        return this.decorationsLayer;
    }

    public TiledLayer getObstaclesLayer() {
        return this.obstaclesLayer;
    }
}
