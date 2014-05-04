package me.riseremi.entities;

import me.riseremi.core.Global;
import me.riseremi.ui.HPBar;


/**
 *
 * @author remi
 */
public class Friend extends Entity {

    //@Getter @Setter private boolean isServer;
    public Friend(String name, int imgId, int id, Entity.Type type) {
        //HARDCODE
        super(name, imgId, id, 0, 572 - HPBar.INDENT - HPBar.BAR_HEIGHT, Global.WINDOW_WIDTH, type);
    }

}
