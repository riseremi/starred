package me.riseremi.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.Setter;
import me.riseremi.cards.Deck;
import me.riseremi.core.Camera;
import me.riseremi.core.Core_v1;
import me.riseremi.core.Global;
import me.riseremi.items.Item;
import me.riseremi.main.Main;
import me.riseremi.map.world.World;
import me.riseremi.ui.HPBar;

/**
 *
 * @author remi
 */
public class Entity {

    @Getter protected ArrayList<Item> inventory = new ArrayList<>();
    @Getter @Setter protected int hp, maxHp, verticalSpeed, horizontalSpeed, pDef, mDef, pAtk, mAtk;
    @Getter @Setter protected String name;
    //координаты измеряются в тайлах
    @Getter @Setter private int x, y; //straight from the top-left corner, 0:0, no offset
    @Getter @Setter protected BufferedImage sprite;
    @Getter @Setter protected boolean isPaint = false;
    @Getter @Setter protected int invSize = 20;
    @Getter @Setter protected float actionPoints;
    //action costs
    public static final float MOVE_COST = 0.5F;
    @Getter protected Deck deck;
    @Getter @Setter private boolean canMove = true;
    @Getter @Setter private HPBar hpBar;
    private int additionAP;
    @Setter private boolean drawHPBar = true;
    @Setter @Getter int id;
    @Getter int imgId;
    @Getter @Setter Type type;

    public enum Type {

        PLAYER, NOT_PLAYER, CREEP, NONE
    }

    public Entity(String name, /*String pathToSprite,*/ int imgId, int id, int barX, int barY, int maxWidth, Type type) {
        hpBar = new HPBar(barX, barY, HPBar.INDENT, maxWidth, HPBar.BAR_HEIGHT);
        this.imgId = imgId;
        this.id = id;
        this.deck = new Deck();
        this.actionPoints = 10;
        this.hp = this.maxHp = 30;
        this.pAtk = 0;
        this.pDef = 0;
        this.mDef = 0;
        this.type = type;
        this.setPaint(true);
        //this.setName(name + " (id: " + id + ")");
        this.setName(name);
        try {
            //this.setSprite(ImageIO.read(getClass().getResourceAsStream(pathToSprite)));
            this.setSprite(ImageIO.read(getClass().getResourceAsStream("/res/sprites/hero" + imgId + ".png")));
        } catch (IOException ex) {
            System.out.println("cannot load sprite");
        }
    }

    //отрисовка героя и его друга, isPaint - рисовать ли (не рисуем, пока друг не подключился к игре)    
    //координаты друга транслируем в соответствии с положением карты (абсолютная коорд друга + абсолютная коорд мира)
    //герой, за которого мы играем, всегда в центре экрана
    public void paint(Graphics g, World world, boolean isFriend) {
        final Core_v1 instance = Core_v1.getInstance();
        int xo, yo;
        xo = x * Global.tileWidth;
        yo = y * Global.tileHeight;

        if (isPaint) {
            g.drawImage(sprite.getScaledInstance(Global.tileWidth, Global.tileHeight, 0), xo, yo, null);
            g.setColor(Color.WHITE);
            int nameLen2 = g.getFontMetrics().stringWidth(name + " (id: " + id + ")") / 2;
            g.drawString(name + " (id: " + id + ") + x/y: " + x + "/" + y + " : " + instance.getPlayer().getId(), xo + 16 - nameLen2, yo - 8);
        }
        instance.getCamera().untranslate(g);
        hpBar.paint(g, this);
        instance.getCamera().translate(g);
    }

    public void setImage(int imgId) {
        try {
            this.setSprite(ImageIO.read(getClass().getResourceAsStream("/res/sprites/hero" + imgId + ".png")));
        } catch (IOException ex) {
        }
    }

    public void moveDown() {
        y += 1;
    }

    public void moveUp() {
        y -= 1;
    }

    public void moveLeft() {
        x -= 1;
    }

    public void moveRight() {
        x += 1;
    }

    /**
     * @deprecated @param xAdd
     * @param yAdd
     */
    public void addToPosition(int xAdd, int yAdd) {
        x += xAdd;
        y += yAdd;
    }

    public void setPosition(int x, int y) {
        setPosition(x, y, true);
    }

    public void setPosition(int x, int y, boolean check) {
        this.x = x;
        this.y = y;
        Core_v1 core = Core_v1.getInstance();

        //System.out.println("pid: " + core.getPlayer().getId() + "/cid: " + id);
        if (check && id == core.getPlayer().getId()) {
            Camera camera = core.getCamera();
            camera.setX(-(x * Global.tileWidth - Global.CENTER_X * Global.tileWidth));
            camera.setY(-(y * Global.tileHeight - Global.CENTER_Y * Global.tileHeight));
        }
    }

    /**
     * @deprecated @param addX
     */
    public void changeX(int addX) {
        x += addX;
    }

    /**
     * @deprecated @param addY
     */
    public void changeY(int addY) {
        y += addY;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean canDoIt(float cost) {
        return actionPoints - cost >= 0;
    }

    public void decreaseActionPoint(float cost) {
        //actionPoints -= cost;
    }

    public void decreaseBloodCostHP(int cost) {
        hp = (hp - cost < 0) ? 0 : (hp - cost);
    }

    public void resetActionPoints() {
        actionPoints = actionPoints > 0 ? 10 + 1 + additionAP : 10;
        additionAP = 0;
    }

    public void dealPhysicalDamage(int power) {
        Main.addToChat("Got " + (power - pDef) + " damage\r\n");
        hp = (hp - (power - pDef) < 0) ? 0 : (hp - (power - pDef));
    }

    public void dealMagicalDamage(int power) {
        Main.addToChat("Got " + (power - mDef) + " damage\r\n");
        hp = (hp - (power - mDef) < 0) ? 0 : (hp - (power - pDef));
    }

    public void dealPureDamage(int power) {
        hp = (hp - power <= maxHp) ? 0 : hp - power;
    }

    public void heal(int power) {
        hp = (hp + power <= maxHp) ? hp + power : maxHp;
    }

    public void waitTurn() {
        Main.addToChat("Turn skipped.\r\n");
    }

    public void addAPInNextTurn(int power) {
        additionAP += power;
    }

}
