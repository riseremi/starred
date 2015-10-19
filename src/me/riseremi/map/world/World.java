package me.riseremi.map.world;

import me.riseremi.map.layer.TiledLayer;
import me.riseremi.core.Global;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import lombok.*;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public final class World {

    private @Getter TiledLayer backgroundLayer;
    private @Getter TiledLayer decorationsLayer;
    private @Getter TiledLayer obstaclesLayer;

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

}
