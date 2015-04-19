package me.riseremi.map.world;

import me.riseremi.map.layer.TiledLayer;
import me.riseremi.core.Global;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import lombok.*;

/**
 *
 * @author remi
 */
public final class World {

    private @Getter TiledLayer worldLayer;
    private @Getter TiledLayer objectsLayer;
    private @Getter TiledLayer nullLayer;

    public World(int tileWidth, int tileHeight, int width, int height) {
        try {
            nullLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
            worldLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
            objectsLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
        } catch (IOException ex) {
        }
    }

    public void draw(Graphics g) {
        worldLayer.paintLayer(g);
        objectsLayer.paintLayer(g);
    }

    @Deprecated
    public int getX() {
        return worldLayer.getBlocksX();
    }

    @Deprecated
    public int getY() {
        return worldLayer.getBlocksY();
    }

}
