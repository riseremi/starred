package me.riseremi.controller;

import me.riseremi.core.Core_v1;
import me.riseremi.main.Main;
import me.riseremi.entities.Player;
import me.riseremi.map.world.World;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author remi
 */
public class Controller implements KeyListener {

    private boolean isChat;
    private static Random rnd = new Random();

    public Controller(boolean isChat) {
        this.isChat = isChat;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        Player player = Core_v1.getInstance().getPlayer();
        World world = Core_v1.getInstance().getWorld();

        //блокировка передвижения, пока не подключится клиент
        //if (Core_v1.getInstance().isConnected()) {
        if (!isChat && Core_v1.getInstance().isNextTurnAvailable()) {
            try {
                HeroController.heroController(player, world, ke);
            } catch (CloneNotSupportedException ex) {
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //}

        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            Main.toggleChat();
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}
