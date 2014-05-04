package me.riseremi.map.layer;

import me.riseremi.core.Core_v1;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author remi
 */
public class SpriteSheet {

    private BufferedImage[] tiles = null;

    public SpriteSheet() {
        try {
            tiles = Core_v1.getInstance().getWorld().getWorldLayer().chopImage(ImageIO.read(getClass().getResourceAsStream("/res/items.png")));
        } catch (IOException ex) {
        }
    }

    public BufferedImage getImage(int id) {
        return tiles[id];
    }
}
