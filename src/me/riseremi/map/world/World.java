package me.riseremi.map.world;

import me.riseremi.items.Item;
import me.riseremi.map.layer.TiledLayer;
import me.riseremi.core.Global;
import java.awt.*;
import java.io.*;
import java.util.*;
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
    private @Getter ArrayList<Item> items = new ArrayList<>();

    public World(int tileWidth, int tileHeight, int width, int height) {
        try {
            nullLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
            worldLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
            objectsLayer = new TiledLayer(ImageIO.read(getClass().getResourceAsStream(Global.pathToTheTiles)), tileWidth, tileHeight, width, height);
        } catch (IOException ex) {
        }
    }

    public void draw(Graphics g) {
        final int blocksX = 0;
        final int blocksY = 0;
        
        //old code when camera didn't exist
//        final int blocksX = worldLayer.getBlocksX();
//        final int blocksY = worldLayer.getBlocksY();
//        nullLayer.setBlocksX(blocksX);
//        nullLayer.setBlocksY(blocksY);
//        objectsLayer.setBlocksX(blocksX);
//        objectsLayer.setBlocksY(blocksY);

        worldLayer.paintLayer(g);
        objectsLayer.paintLayer(g);
    }

    public void changePosition(int addX, int addY) {
//        worldLayer.setBlocksY(worldLayer.getBlocksY() - addY);
//        //Core_v1.getInstance().getPlayer().changeX(-addX);
//
//        worldLayer.setBlocksX(worldLayer.getBlocksX() - addX);
//        //Core_v1.getInstance().getPlayer().changeY(-addY);
//
//        //Core_v1.getInstance().getPlayer().addToPosition(addX, addY);
    }

    public int getX() {
        return worldLayer.getBlocksX();
    }

    public int getY() {
        return worldLayer.getBlocksY();
    }

}
