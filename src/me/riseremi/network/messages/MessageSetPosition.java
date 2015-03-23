package me.riseremi.network.messages;

import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import me.riseremi.main.Main;
import org.rising.framework.network.Message;

/**
 *
 * @author Remi
 */
public class MessageSetPosition extends Message {

    private final int id, x, y;

    public MessageSetPosition(int id, int x, int y) {
        super(Message.Type.SET_POSITION);
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void processServer(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processClient(Message message) {
        MessageSetPosition msgSP = ((MessageSetPosition) message);
        System.out.println(msgSP.getId());
        final Entity entity = Core_v1.getInstance().getPlayerById(msgSP.getId());
        final int xx = msgSP.getX();
        final int yy = msgSP.getY();
        if (Main.ENABLE_DEBUG_TOOLS) {
            System.out.println("Teleported to " + xx + ":" + yy);
        }
        entity.setPosition(x, y);
    }

}
