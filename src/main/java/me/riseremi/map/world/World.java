package me.riseremi.map.world;

import me.riseremi.core.Global;
import me.riseremi.map.layer.TiledLayer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static me.riseremi.utils.ResourceUtilsKt.loadImage;

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
            BufferedImage tileset = loadImage(Global.pathToTheTiles);

            obstaclesLayer = new TiledLayer(tileset, tileWidth, tileHeight, width, height);
            backgroundLayer = new TiledLayer(tileset, tileWidth, tileHeight, width, height);
            decorationsLayer = new TiledLayer(tileset, tileWidth, tileHeight, width, height);
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
