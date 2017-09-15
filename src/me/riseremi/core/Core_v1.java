package me.riseremi.core;

import lombok.Getter;
import lombok.Setter;
import me.riseremi.cards.Card;
import me.riseremi.cards.CardsArchive;
import me.riseremi.cards.DrawableCard;
import me.riseremi.cards.Hand;
import me.riseremi.controller.mouse.MouseController;
import me.riseremi.controller.mouse.SelectionCursor;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Friend;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
import me.riseremi.map.world.World;
import me.riseremi.mreader.StarredMap;
import me.riseremi.mreader.WrongFormatException;
import me.riseremi.network.messages.MessageConnect;
import me.riseremi.network.messages.MessageEndTurn;
import me.riseremi.network.messages.MessageGameOver;
import me.riseremi.ui.windows.Window;
import org.rising.framework.network.Client;
import org.rising.framework.network.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author riseremi <riseremi at icloud.com>
 */
public final class Core_v1 extends JPanel {

    private @Getter
    @Setter
    Player player;
    private @Getter
    Friend friend;
    private @Getter
    World world;
    private @Getter
    Server server;
    private @Getter
    Client client;
    private final ArrayList<Window> windows = new ArrayList<>();
    private static Core_v1 instance;
    private @Getter
    @Setter
    boolean connected = false;
    private BufferedImage waitingImage;
    @Getter
    @Setter
    private boolean nextTurnAvailable;
    private Font walkwayBold;
    //
    @Setter
    @Getter
    private boolean cardJustUsed;
    private long effectStartTime;
    //
    @Getter
    @Setter
    private boolean tileSelectionMode = false;
    private long turnStartTime;
    private long timeLeft = 1;
    @Getter
    private boolean serverMode;
    //
    private boolean initialized = false;
    private final long TURN_TIME_LIMIT = 3 * 60 * 1000; //3 minutes
    @Getter
    @Setter
    private Camera camera;
    @Getter
    @Setter
    private SelectionCursor selectionCursor;
    @Getter
    @Setter
    private int cardsDrawn = 0, cardsDrawnLimit = 30;
    @Setter
    private boolean gameOver;
    @Setter
    private int winnerId;

    public static Core_v1 getInstance() {
        if (instance == null) {
            instance = new Core_v1();
        }
        return instance;
    }

    private Core_v1() {
    }

    public void init(int imgId, String ip, String name, boolean isServer) {
        serverMode = isServer;
        player.setName(name);
        friend.setName(name);
        if (isServer) {
            initServer(imgId, name);
        } else {
            Server.SERVER_IP = ip;
            initClient(imgId, ip, name);
        }
    }

    public synchronized void initBase() {
        walkwayBold = new Font("Arial", Font.PLAIN, 12);

        world = new World(Global.tileWidth, Global.tileHeight, Global.horizontalTiles, Global.verticalTiles);
        player = new Player("Server", 0, -1, Entity.Type.PLAYER);
        friend = new Friend("Client", 0, -1, Entity.Type.PLAYER);
        camera = new Camera();
        selectionCursor = new SelectionCursor(world, this);

        try {
            StarredMap map = new StarredMap(Global.pathToTheMap);

            world.getObstaclesLayer().setMap(map.getObstaclesLayer());
            world.getBackgroundLayer().setMap(map.getBackgroundLayer());
            world.getDecorationsLayer().setMap(map.getDecorationsLayer());

            player.getHand().addCard(CardsArchive.Companion.getInstance().getCard(Card.BLINK).toDrawableCard());
            player.getHand().addCard(CardsArchive.Companion.getInstance().getCard(Card.BLINK).toDrawableCard());
            player.getHand().addCard(CardsArchive.Companion.getInstance().getCard(Card.FIREBALL).toDrawableCard());
            player.getHand().addCard(CardsArchive.Companion.getInstance().getCard(Card.FIREBALL).toDrawableCard());

            Main.addToChat("System: Listen closely.\n\r");
            Main.addToChat("System: The highways call my name.\n\r");
        } catch (WrongFormatException ex) {
            Logger.getLogger(Core_v1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMouseListener(new MouseController());
        addMouseMotionListener(new MouseController());
    }

    // init both server and client
    // need to recode to get standalone server
    public void initServer(int imgId, String name) {
        player.setImage(imgId);
        Main.main.setTitle(Main.GAME_TITLE + " - Server");

        Server.SERVER_IP = "localhost";
        server = Server.getInstance();
        client = Client.getInstance();

        try {
            client.send(new MessageConnect(player.getName(), imgId));
            Main.getLobbyScreen().getPlayersListModel().addElement(player.getName());
        } catch (IOException ex) {
        }
        friend.setPosition(Global.CENTER_X + 15, Global.CENTER_Y + 5, false);
        player.setPosition(Global.CENTER_X + 5, Global.CENTER_Y + 5);
    }

    public void initClient(int imgId, String ip, String name) {
        player.setImage(imgId);
        Main.main.setTitle(Main.GAME_TITLE + " - Client");
        client = Client.getInstance();
        try {
            client.send(new MessageConnect(player.getName(), imgId));
            Main.getLobbyScreen().getPlayersListModel().addElement(player.getName());
        } catch (Exception ex) {
        }
        friend.setPosition(Global.CENTER_X + 5, Global.CENTER_Y + 5, false);
        player.setPosition(Global.CENTER_X + 15, Global.CENTER_Y + 5);

        player.getHand().addCard(CardsArchive.Companion.getInstance().getCard(Card.BLINK).toDrawableCard());
    }

    @Override
    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;

        g.setFont(walkwayBold);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        try {
            Thread.sleep(15L);
        } catch (InterruptedException ignored) {
        }

        g.setColor(Color.black);
        g.fillRect(0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);

        //translate viewport and draw world and players
        camera.translate(g);
        world.draw(g);

        player.paint(g2, world, false);
        friend.paint(g2, world, true);

        camera.untranslate(g);

        player.getHpBar().paint(g2, player);
        friend.getHpBar().paint(g2, friend);

        final Player player1 = player;
        final Hand hand = player1.getHand();

        DrawableCard activeCard = hand.getActiveCard();

        //draw card use radius
        final Card raisedCard = player.getRaisedCard();
        if (activeCard != null || raisedCard != null) {
            final Card card = activeCard != null ? activeCard.toCard() : raisedCard;
            final int minRadius = card.getRange()[0];
            final int radius = card.getRange()[1];

            final int x = player.getX();
            final int y = player.getY();

            final int xo = x * Global.tileWidth;
            final int yo = y * Global.tileHeight;

            g.setColor(new Color(231, 76, 60, 50));

            camera.translate(g);
            for (int w = 1; w < radius * 2 + 1; w += 2) {
                g.fillRect(xo - w / 2 * 32, yo - (radius - w / 2) * 32, w * 32, 32);
            }

            for (int w = radius * 2 + 1; w > 0; w -= 2) {
                g.fillRect(xo - w / 2 * 32, yo + (radius - w / 2) * 32, w * 32, 32);
            }
            //test draw min range
            g.setColor(new Color(52, 152, 219, 50));

            for (int w = 1; w < minRadius * 2 + 1; w += 2) {
                g.fillRect(xo - w / 2 * 32, yo - (minRadius - w / 2) * 32, w * 32, 32);
            }

            for (int w = minRadius * 2 + 1; w > 0; w -= 2) {
                g.fillRect(xo - w / 2 * 32, yo + (minRadius - w / 2) * 32, w * 32, 32);
            }
            camera.untranslate(g);
        }
        player.getHand().paint(g);
        g.setFont(walkwayBold);

        if (tileSelectionMode) {
            selectionCursor.paint(g2);

            g.setColor(Color.WHITE);
            String cursorCoordinates = selectionCursor.getRealX() + ":" + selectionCursor.getRealY();
            g.drawString(cursorCoordinates, selectionCursor.getX(), selectionCursor.getY());
        }

        g.setColor(Color.WHITE);
        if (nextTurnAvailable) {
            g.drawString("Your turn (F11 end turn)", 32, 64);

            timeLeft = TURN_TIME_LIMIT - (System.currentTimeMillis() - turnStartTime) + 1000;
            String newString = String.format("%02d min, %02d sec",
                    TimeUnit.MILLISECONDS.toMinutes(timeLeft),
                    TimeUnit.MILLISECONDS.toSeconds(timeLeft)
                            - TimeUnit.MILLISECONDS.toMinutes(timeLeft) * 60);

            g.drawString("Time left: " + newString, 32, 64 + 24);
        } else {
            g.drawString("Enemy turn", 32, 64);
        }
        g.drawString("AP: " + player.getActionPoints(), 32, 64 + 24 * 2);
        g.drawString("Cards drawn: " + cardsDrawn, 32, 64 + 24 * 3);

        if (timeLeft <= 0) {
            try {
                endTurn();
                timeLeft = TURN_TIME_LIMIT;
            } catch (IOException ex) {
            }
        }

        //show game over message
        if (player.isDead() || friend.isDead()) {
            nextTurnAvailable = false;
            gameOver = true;
            winnerId = player.isDead() ? friend.getId() : player.getId();
        }

        //game over if cardsDrawn > cardsDrawnLimit
        if (cardsDrawn >= cardsDrawnLimit && !gameOver) {
            int playerHP = player.getHp();
            int friendHP = friend.getHp();
            int winner = playerHP > friendHP ? player.getId() : friend.getId();
            MessageGameOver message = new MessageGameOver(winner);

            try {
                Client.getInstance().send(message);
            } catch (IOException ex) {
            }
        }

        if (gameOver) {
            nextTurnAvailable = false;

            int overlayHeight = Global.VIEWPORT_HEIGHT / 5;

            g.setColor(new Color(0, 0, 0, 0.5f));
            g.fillRect(0, Global.VIEWPORT_HEIGHT / 2 - overlayHeight / 2, Global.WINDOW_WIDTH, overlayHeight);

            Font trb = new Font("Arial", Font.BOLD, 28);
            g.setFont(trb);

            g.setColor(Color.WHITE);

            String message = getPlayerById(winnerId).getName() + " wins";
            int xPosition = Global.WINDOW_WIDTH / 2
                    - g.getFontMetrics().stringWidth(message) / 2;

            g.drawString(message, xPosition, 310);
        }

        g.setFont(walkwayBold);

        if (!initialized) {
            initialized = true;
            if (serverMode) {
                startTurn();
            }
        }

        if (!cardJustUsed) {
            effectStartTime = System.currentTimeMillis();
        }

        if (cardJustUsed && !gameOver) {
            g.setColor(Color.MAGENTA);
            g.drawString("[PEWPEW AMAZING EFFECTS]", MouseController.getMouseRect().x - 64, MouseController.getMouseRect().y - 8);
            if (System.currentTimeMillis() - effectStartTime > 2000) {
                cardJustUsed = false;
            }
        }

        repaint();
    }

    /**
     * End current turn, reset player's ADD_AP (Action Points) to its default
     * value (10), disable all actions
     *
     * @throws java.io.IOException
     */
    public void endTurn() throws IOException {
        Main.addToChat("DEBUG: Ending turn...\r\n");
        Client.getInstance().send(new MessageEndTurn());
        player.resetActionPoints();
        nextTurnAvailable = !nextTurnAvailable;
    }

    /**
     * Enable all actions, add a new card
     */
    public void startTurn() {
        player.getHand().addCard(CardsArchive.Companion.getInstance().getRandomCard().toDrawableCard());
        incrementCardsDrawn();
        nextTurnAvailable = true;
        turnStartTime = System.currentTimeMillis();
    }

    public boolean rangeMatches(Player player, int realX, int realY, Card card) {
        final int minRange = card.getRange()[0];
        final int maxRange = card.getRange()[1];

        final int userX = player.getX();
        final int userY = player.getY();

        final int xDif = Math.abs(userX - realX);
        final int yDif = Math.abs(userY - realY);

        boolean maxPassed = (xDif + yDif <= maxRange);
        boolean minPassed = (xDif + yDif > minRange);

        //some amazing maths
        return maxPassed && (minRange == 0 || minPassed);
    }

    public Entity getPlayerById(int id) {
        if (Main.ENABLE_DEBUG_TOOLS) {
            System.out.println("GET_ENTITY; player id: " + player.getId() + ", friend id: " + friend.getId() + ", requested id: " + id);
        }
        if (player.getId() == id) {
            return player;
        } else if (friend.getId() == id) {
            return friend;
        } else {
            throw new NullPointerException("Entity with specified id doesn't exist.");
        }
    }

    public void incrementCardsDrawn() {
        cardsDrawn++;
    }

}
