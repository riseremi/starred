package me.riseremi.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import lombok.Getter;
import lombok.Setter;
import me.riseremi.cards.BasicCard;
import me.riseremi.cards.CardsArchive;
import me.riseremi.cards.Deck;
import me.riseremi.controller.mouse.MouseController;
import me.riseremi.controller.mouse.SelectionCursor;
import me.riseremi.entities.Entity;
import me.riseremi.entities.Friend;
import me.riseremi.entities.Player;
import me.riseremi.main.Main;
import me.riseremi.map.layer.IOManager;
import me.riseremi.map.world.World;
import me.riseremi.network.messages.MessageConnect;
import me.riseremi.network.messages.MessageEndTurn;
import me.riseremi.ui.windows.Window;
import org.rising.framework.network.Client;
import org.rising.framework.network.Server;

/**
 *
 * @author remi
 */
public final class Core_v1 extends JPanel {

    private @Getter @Setter Player player;
    private @Getter Friend friend;
    private @Getter World world;
    private @Getter Server server;
    private @Getter Client client;
    private ArrayList<Window> windows = new ArrayList<>();
    private static Core_v1 instance;
    private @Getter @Setter boolean connected = false;
    private BufferedImage waitingImage;
    @Getter @Setter private boolean nextTurnAvailable;
    private Font walkwayBold;
    //
    @Setter @Getter private boolean cardJustUsed;
    private long effectStartTime;
    //
    @Getter @Setter private boolean tileSelectionMode = false;
    @Getter @Setter private Rectangle selectionCursor = new Rectangle();
    private long turnStartTime;
    private long timeLeft = 1;
    private boolean iAmServer;
    //
    private boolean initialized = false;
    private final long TURN_TIME_LIMIT = 3 * 60 * 1000; //3 minutes
    @Getter @Setter private Camera camera;
    @Getter @Setter private SelectionCursor selectionCursorv2 = new SelectionCursor();
    @Getter @Setter private boolean selectionMode;

    public static Core_v1 getInstance() {
        if (instance == null) {
            instance = new Core_v1();
        }
        return instance;
    }
    private boolean gameOver;

    private Core_v1() {
    }

    public void init(int imgId, String ip, String name, boolean isServer) {
        iAmServer = isServer;
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
        player = new Player("Starred - Server", 0, -1, Entity.Type.PLAYER);
        friend = new Friend("Starred - Client", 0, -1, Entity.Type.PLAYER);
        camera = new Camera();

        try {
            IOManager.newLoadFromFileToVersion2(Global.pathToTheMap, world);
            waitingImage = ImageIO.read(getClass().getResourceAsStream("/res/waiting.png"));
            player.getDeck().addCard(CardsArchive.getRandomCard());
            player.getDeck().addCard(CardsArchive.get(BasicCard.BLINK));
            Main.addToChat("System: Listen closely.\n\r");
            Main.addToChat("System: The highways call my name.\n\r");
        } catch (IOException | CloneNotSupportedException ex) {
            System.out.println(ex.toString());
        }
        addMouseListener(new MouseController());
        addMouseMotionListener(new MouseController());
//        addMouseListener(new MouseController());
//        addMouseMotionListener(new MouseController());
    }

    //inits both server and client
    //need to recode to get standalone server
    public void initServer(int imgId, String name) {
        player.setImage(imgId);
        //player.setName(name.isEmpty() ? "Server" : name);
        Main.game.setTitle("Server");

        Server.SERVER_IP = "localhost";
        server = Server.getInstance();
        client = Client.getInstance();

        try {
            client.send(new MessageConnect(player.getName()));
        } catch (IOException ex) {
        }
        friend.setPosition(Global.CENTER_X + 15, Global.CENTER_Y + 5, false);
        player.setPosition(Global.CENTER_X + 5, Global.CENTER_Y + 5);
    }

    public void initClient(int imgId, String ip, String name) {
        player.setImage(imgId);
        Main.game.setTitle("Client");
        client = Client.getInstance();

        try {
            client.send(new MessageConnect(player.getName()));
        } catch (IOException ex) {
        }
        friend.setPosition(Global.CENTER_X + 5, Global.CENTER_Y + 5, false);
        player.setPosition(Global.CENTER_X + 15, Global.CENTER_Y + 5);
    }

    /**
     *
     * @param g2
     */
    @Override
    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;

        g.setFont(walkwayBold);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        try {
            Thread.sleep(15L);
        } catch (InterruptedException ex) {
        }

        g.setColor(Color.black);
        g.fillRect(0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT);

        //translate viewport and draw world and players
        camera.translate(g);
        world.draw(g);

        player.paint(g2, world, false);
        friend.paint(g2, world, true);

        camera.untranslate(g);

        if (isSelectionMode()) {
            selectionCursorv2.paint(g);
        }

        final Player player1 = player;
        final Deck deck = player1.getDeck();

        BasicCard activeCard = deck.getActiveCard();

        //draw card radius
        final BasicCard justUsedCard = player.getDeck().getJustUsedCard();
        if (activeCard != null || (justUsedCard != null && !Arrays.equals(justUsedCard.getEffects(), new BasicCard.EffectType[]{BasicCard.EffectType.NONE}))) {
            final int radius = activeCard != null ? activeCard.getUseRadius() : justUsedCard.getUseRadius();

            final int x = player.getX();
            final int y = player.getY();

            final int xo = x * Global.tileWidth;
            final int yo = y * Global.tileHeight;

            g.setColor(new Color(200, 0, 0, 40));

            camera.translate(g);
            for (int w = 1; w < radius * 2 + 1; w += 2) {
                g.fillRect(xo - w / 2 * 32, yo - (radius - w / 2) * 32, w * 32, 32);
            }

            for (int w = radius * 2 + 1; w > 0; w -= 2) {
                g.fillRect(xo - w / 2 * 32, yo + (radius - w / 2) * 32, w * 32, 32);
            }
            camera.untranslate(g);
        }
        player.getDeck().paint(g);
        g.setFont(walkwayBold);

        if (tileSelectionMode) {
            g.setColor(Color.red);
            g.drawRect(selectionCursor.x, selectionCursor.y, 32, 32);
        }

        g.setColor(Color.WHITE);
        if (nextTurnAvailable) {
            g.drawString("YOUR TURN (F11 end turn)", 32, 64);

            timeLeft = TURN_TIME_LIMIT - (System.currentTimeMillis() - turnStartTime) + 1000;
            String newString = String.format("%02d min, %02d sec",
                    TimeUnit.MILLISECONDS.toMinutes(timeLeft),
                    TimeUnit.MILLISECONDS.toSeconds(timeLeft)
                    - TimeUnit.MILLISECONDS.toMinutes(timeLeft) * 60);

            g.drawString("Time left: " + newString, 32, 64 + 24);
        } else {
            g.drawString("ENEMY TURN", 32, 64);
        }
        g.drawString("AP: " + player.getActionPoints(), 32, 64 + 24 * 2);

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

            int overlayHeight = Global.VIEWPORT_HEIGHT / 5;

            g.setColor(new Color(0, 0, 0, 0.5f));
            g.fillRect(0, Global.VIEWPORT_HEIGHT / 2 - overlayHeight / 2,
                    Global.WINDOW_WIDTH, overlayHeight);

            Font trb = new Font("Arial", Font.BOLD, 28);
            g.setFont(trb);

            g.setColor(Color.WHITE);

            String message = player.isDead() ? friend.getName() + " wins"
                    : player.getName() + " wins";
            g.drawString(message, 400, 310);
        }

        g.setFont(walkwayBold);

        if (!initialized) {
            initialized = true;
            if (iAmServer) {
                startTurn();
            }
        }

        if (!cardJustUsed) {
            effectStartTime = System.currentTimeMillis();
        }

        if (cardJustUsed && !gameOver) {
            g.setColor(Color.MAGENTA);
            g.drawString("PEWPEW AMAZING EFFECTS", MouseController.getMouseRect().x - 64, MouseController.getMouseRect().y - 8);
            if (System.currentTimeMillis() - effectStartTime > 2000) {
                cardJustUsed = false;
            }
        }

        repaint();
    }

    /**
     * End current turn, reset player's ADD_AP (Action Points) to its default
     * value (10), disables all actions
     *
     * @throws java.io.IOException
     */
    public void endTurn() throws IOException {
        Client.getInstance().send(new MessageEndTurn());
        player.resetActionPoints();
        nextTurnAvailable = false;
    }

    /**
     * enable all actions, add a new card
     */
    public void startTurn() {
        try {
            player.getDeck().addCard(CardsArchive.getRandomCard());
        } catch (CloneNotSupportedException ex) {
        }
        nextTurnAvailable = true;
        turnStartTime = System.currentTimeMillis();
    }

    public boolean isTheyNear(Player player, int fx, int fy, int radius) {
        final int px = player.getX();
        final int py = player.getY();

        final int xDif = Math.abs(px - fx);
        final int yDif = Math.abs(py - fy);

        //some amazing maths
        return (xDif + yDif <= radius);
    }

    public Entity getPlayerById(int id) {
        //System.out.println("GET_ENTITY; player id: " + player.getId() + ", friend id: " + friend.getId() + ", requested id: " + id);
        if (player.getId() == id) {
            return player;
        } else if (friend.getId() == id) {
            return friend;
        } else {
            throw new NullPointerException("Entity with specified id doesn't exist.");
        }
    }

}
