package me.riseremi.entities;

import me.riseremi.core.Global;


/**
 *
 * @author remi
 */
public class Player extends Entity {

    //@Getter @Setter private boolean isServer;
    public Player(String name, int imgId, int id, Entity.Type type) {
        super(name, imgId, id, 0, 0, Global.WINDOW_WIDTH, type);
    }

}
