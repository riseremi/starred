package me.riseremi.ui.windows;

import me.riseremi.core.Core_v1;
import me.riseremi.items.Item;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author remi
 */
public class Inventory extends Window {

    public Inventory(String title, int width, int height, int titleX) {
        super(title, width, height, titleX);
    }

    @Override
    public void paint(Graphics g) {
        ArrayList<Item> inv = Core_v1.getInstance().getPlayer().getInventory();
        super.paint(g);
        if (isVisible()) {
            for (Item i : inv) {
                g.drawString(i.getName(), 64, inv.indexOf(i) * 16 + 64);
            }
        }
    }
}
