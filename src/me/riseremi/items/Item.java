package me.riseremi.items;

import java.awt.Color;
import java.awt.Graphics;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.riseremi.core.Global;

/**
 *
 * @author remi
 */
@RequiredArgsConstructor @AllArgsConstructor public class Item {

    private @Getter String name;
    private @Getter int id;
    private @Getter int x, y;

    public void paint(Graphics g) {
        g.drawImage(Global.items.getImage(id), Global.translateX(x) * Global.tileWidth, Global.translateY(y) * Global.tileHeight, null);

        g.setColor(Color.WHITE);
        g.drawString(name, Global.translateX(x) * Global.tileWidth, Global.translateY(y) * Global.tileHeight);
        g.drawString(x + ":" + y, Global.translateX(x) * Global.tileWidth, Global.translateY(y) * Global.tileHeight + 12);
    }
}
