package me.riseremi.entities;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class EntityList {
    @Getter @Setter private ArrayList<Entity> entities = new ArrayList<>();

    public EntityList() {
    }

    public void add(Entity e) {
//        boolean alreadyExists = false;
//        for (Entity entity : entities) {
//            if (e.getId() == entity.getId()) {
//                alreadyExists = true;
//            }
//        }
//        if (!alreadyExists) {
        entities.add(e);
//        }
    }

    public Entity get(int id) {
        for (Entity entity : entities) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    public void remove(Entity e) {
        entities.remove(e);
    }

    public int indexOf(Entity e) {
        return entities.indexOf(e);
    }

}
