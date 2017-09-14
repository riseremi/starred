package me.riseremi.controller;

import me.riseremi.cards.Card;
import me.riseremi.cards.CardsArchivev3;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
import me.riseremi.map.world.World;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author riseremi <riseremi at icloud.com>
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
        if (!isChat && Core_v1.getInstance().isNextTurnAvailable()) {
            try {
                HeroController.heroController(player, world, ke);
            } catch (CloneNotSupportedException ex) {
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            Main.toggleChat();
        }

        if (Main.ENABLE_DEBUG_TOOLS && !isChat && ke.getKeyCode() == KeyEvent.VK_G) {
            String response = JOptionPane.showInputDialog(null,
                    "Card id:",
                    "",
                    JOptionPane.QUESTION_MESSAGE);

            try {
                final int cardId = Integer.parseInt(response);
                final Card card = CardsArchivev3.Companion.getInstance().getCard(cardId);

                Core_v1.getInstance().getPlayer().getHand().addCard(card.toDrawableCard());
            } catch (NumberFormatException ex) {
                System.out.println("ERROR: cannot give card (wrong id)");
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}
