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
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import lombok.Getter;
import lombok.Setter;
import me.riseremi.cards.BasicCard;
import me.riseremi.cards.CardsArchive;
import me.riseremi.cards.Deck;
import me.riseremi.controller.MouseController;
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

    //private boolean paintAll = true;
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
    //cards preview on the right side
    //@Getter private CardSmall[] cardSmalls = new CardSmall[3];
    //private BufferedImage cardSmall;
    private Font walkwayBold;
    //
    @Setter @Getter private boolean cardJustUsed;
    private long effectStartTime;
    //
    //@Getter @Setter private BasicCard.Effect effect = BasicCard.Effect.NONE;
    @Getter @Setter private boolean tileSelectionMode = false;
    @Getter @Setter private Rectangle selectionCursor = new Rectangle();
    private long turnStartTime;
    private long timeLeft = 1;
    private boolean iAmServer;
    //
    // private String[] cardImagesPath = {"/res/card_magic.png", "/res/card_physical.png", "/res/card_blink.png",};
    private boolean initialized = false;
    private final long TURN_TIME_LIMIT = 3 * 60 * 1000;
    //creeps (player and friend are creeps too)
    //@Getter @Setter private ArrayList<Entity> entities = new ArrayList<>();
    //@Getter @Setter private EntityList entities = new EntityList();
//    @Getter @Setter private int playerId;
    @Getter @Setter private Camera camera;

    public static Core_v1 getInstance() {
        if (instance == null) {
            instance = new Core_v1();
        }
        return instance;
    }

    private Core_v1() {
    }

    public void init(int imgId, String ip, String name, boolean isServer) {
        iAmServer = isServer;
        if (isServer) {
            initServer(imgId, name);
        } else {
            initClient(imgId, ip, name);
        }
    }

    public synchronized void initBase() {
        walkwayBold = new Font("Arial", Font.PLAIN, 12);

        world = new World(Global.tileWidth, Global.tileHeight, Global.horizontalTiles, Global.verticalTiles);
        player = new Player("Server", 0, -1, Entity.Type.PLAYER);
        friend = new Friend("Client", 1, -1, Entity.Type.PLAYER);
        camera = new Camera();

        try {
            IOManager.newLoadFromFileToVersion2(Global.pathToTheMap, world);
            waitingImage = ImageIO.read(getClass().getResourceAsStream("/res/waiting.png"));
            player.getDeck().addCard(CardsArchive.getRandomCard());
            player.getDeck().addCard(CardsArchive.get(BasicCard.BLINK_ID));
            Main.addToChat("System: Listen closely.\n\r");
            Main.addToChat("System: Prepare your anus.\n\r");
        } catch (IOException | CloneNotSupportedException ex) {
            System.out.println(ex.toString());
        }
        addMouseListener(new MouseController());
        addMouseMotionListener(new MouseController());
    }

    //inits both server and client
    //need to recode to get standalone server
    public void initServer(int imgId, String name) {
//        player.setImage(imgId);
//        //
        //
        //entities.add(player);
        //entities.add(friend);
        //Creep testCreep = new Creep("testCreep", 3, entities.getEntities().size(), Entity.Type.CREEP);
        //testCreep.setY(18);
        //testCreep.setX(18);
        //entities.add(testCreep);
        //
        player.setName(name.isEmpty() ? "Server" : name);
        //player.setServer(false);
        Main.game.setTitle("Server");

        Server.SERVER_IP = "localhost";
        server = Server.getInstance();
        client = Client.getInstance();

        //client.setId(0);
        try {
            //client.setId(1);
            client.send(new MessageConnect(player.getName()));
        } catch (IOException ex) {
        }
        friend.setPosition(Global.CENTER_X + 15, Global.CENTER_Y + 5, false);
        player.setPosition(Global.CENTER_X + 5, Global.CENTER_Y + 5);
    }

    public void initClient(int imgId, String ip, String name) {
        player.setImage(imgId);

        //entities.add(friend);
        //entities.add(player);
        //friend.setId(entities.indexOf(friend));
        //player.setId(entities.indexOf(player));
        //
        Main.game.setTitle("Client");
        //
        //player.setName(name.isEmpty() ? "Client" : name);
        //friend.setServer(false);

        //friend.setPaint(true);
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

        final Player player1 = player;
//        if (player == null) {
//            System.out.println("OMGOMGOMGOMGMOGMGO PLAYER IS NULL");
//        }
        final Deck deck = player1.getDeck();

        BasicCard activeCard = deck.getActiveCard();

        //draw card radius
        final BasicCard justUsedCard = player.getDeck().getJustUsedCard();
        if (activeCard != null || (justUsedCard != null && !justUsedCard.getEffect().equals(BasicCard.Effect.NONE))) {
            final int radius = activeCard != null ? activeCard.getUseRadius() : justUsedCard.getUseRadius();

            final int x = player.getX();
            final int y = player.getY();

            final int xo = x * Global.tileWidth;
            final int yo = y * Global.tileHeight;
//            final int xo = Global.translateX(x) * Global.tileWidth;
//            final int yo = Global.translateY(y) * Global.tileHeight;

            g.setColor(new Color(200, 0, 0, 40));

            camera.translate(g);
            for (int w = 1; w < radius * 2 + 1; w += 2) {
                g.fillRect(xo - w / 2 * 32, yo - (radius - w / 2) * 32, w * 32, 32);
//                g.fillRect(xo - w / 2 * 32, yo - (radius - w / 2) * 32, w * 32, 32);
            }

            for (int w = radius * 2 + 1; w > 0; w -= 2) {
                g.fillRect(xo - w / 2 * 32, yo + (radius - w / 2) * 32, w * 32, 32);
            }
            camera.untranslate(g);
        }
//        if (isConnected()) {
//            synchronized (entities.getEntities()) {
//                for (Entity e : entities.getEntities()) {
//                    e.paint(g, world, false);
//                }
//            }
//        }
        player.getDeck().paint(g);
        g.setFont(walkwayBold);

        if (tileSelectionMode) {
            g.setColor(Color.red);
            g.drawRect(selectionCursor.x, selectionCursor.y, 32, 32);
        }

        g.drawString("AP: " + player.getActionPoints(), 32, 96);

        g.setColor(Color.WHITE);
        if (nextTurnAvailable) {
            g.drawString("YOUR TURN (F12 end turn)", 32, 32);

            timeLeft = TURN_TIME_LIMIT - (System.currentTimeMillis() - turnStartTime) + 1000;
            String newString = String.format("%02d min, %02d sec",
                    TimeUnit.MILLISECONDS.toMinutes(timeLeft),
                    TimeUnit.MILLISECONDS.toSeconds(timeLeft)
                    - TimeUnit.MILLISECONDS.toMinutes(timeLeft) * 60);

            g.drawString("Time left: " + newString, 32, 64);
        } else {
            g.drawString("ENEMY TURN", 32, 32);
        }

        if (timeLeft <= 0) {
            try {
                endTurn();
                timeLeft = TURN_TIME_LIMIT;
            } catch (IOException ex) {
            }
        }

        if (player.isDead()) {
            nextTurnAvailable = false;
            g.drawString("TEH END", 300, 250);
            g.drawString(friend.getName() + " wins\r\n", 300, 270);
        }

        if (friend.isDead()) {
            nextTurnAvailable = false;
            g.drawString("TEH END", 300, 250);
            g.drawString(player.getName() + " wins\r\n", 300, 270);
        }

        /* if (!isConnected()) {
         final int x = (getWidth() >> 1) - (waitingImage.getWidth() >> 1);
         g.drawImage(waitingImage, x, 230, null);
         turnStartTime = System.currentTimeMillis();
         } else */
        if (!initialized) {
            initialized = true;
            if (iAmServer) {
                startTurn();
            }

        }

        if (!cardJustUsed) {
            effectStartTime = System.currentTimeMillis();
        }

        if (cardJustUsed) {
            g.setColor(Color.MAGENTA);
            g.drawString("CARD USED", MouseController.getMouseRect().x - 64, MouseController.getMouseRect().y - 8);
            if (System.currentTimeMillis() - effectStartTime > 2000) {
                cardJustUsed = false;
            }
        }

        repaint();
    }

    /**
     * Ends current turn, resets player's AP (Action Points) to its default
     * value (10), disables all actions
     *
     * @throws java.io.IOException
     */
    public void endTurn() throws IOException {
        //Client.sendData(new TurnEndMessage(playerId));
        Client.getInstance().send(new MessageEndTurn());
        player.resetActionPoints();
        nextTurnAvailable = false;
    }

    /**
     * Starts turn, enables all actions
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

    public Entity getEntity(int id) {
        System.out.println("pl: " + player.getId() + " fr: " + friend.getId() + " id: " + id);
        if (player.getId() == id) {
            return player;
        } else if (friend.getId() == id) {
            return friend;
        } else {
            throw new NullPointerException("Entity with specified id doesn't exist.");
        }
    }

}
