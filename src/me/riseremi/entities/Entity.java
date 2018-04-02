package me.riseremi.entities;

import me.riseremi.cards.Card;
import me.riseremi.cards.CardsArchive;
import me.riseremi.cards.Hand;
import me.riseremi.core.Camera;
import me.riseremi.core.Core_v1;
import me.riseremi.core.Global;
import me.riseremi.main.Main;
import me.riseremi.map.world.World;
import me.riseremi.ui.HPBar;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author riseremi <riseremi at icloud.com>
 */
public class Entity {

    protected int hp, maxHp, pDef, mDef, pAtk;
    protected String name;
    //координаты измеряются в тайлах
    private int x, y; //straight from the top-left corner, 0:0, no offset
    protected BufferedImage sprite;
    protected boolean isPaint = false;
    protected int actionPoints;
    //action costs
    public static final float MOVE_COST = 1F;
    protected Hand hand;
    private boolean canMove = true;
    private HPBar hpBar;
    int id;
    int imgId;
    Type type;
    int classId;
    private final String CLASS_NAMES[] = {"Mage", "Blood Mage", "Head Hunter", "Mage", "Blood Mage", "Head Hunter",
            "Mage", "Blood Mage", "Head Hunter", "Mage", "Blood Mage", "Head Hunter", "Mage", "Blood Mage", "Head Hunter",
            "Mage", "Blood Mage", "Head Hunter", "Mage", "Blood Mage", "Head Hunter", "Mage", "Blood Mage", "Head Hunter"};

    public int getHp() {
        return this.hp;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getActionPoints() {
        return this.actionPoints;
    }

    public Hand getHand() {
        return this.hand;
    }

    public boolean isCanMove() {
        return this.canMove;
    }

    public HPBar getHpBar() {
        return this.hpBar;
    }

    public int getId() {
        return this.id;
    }

    public int getImgId() {
        return this.imgId;
    }

    public Type getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void setPaint(boolean isPaint) {
        this.isPaint = isPaint;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public enum Type {

        PLAYER, NOT_PLAYER, CREEP, NONE
    }

    public Entity(String name, /*String pathToSprite,*/ int imgId, int id, int barX, int barY, int maxWidth, Type type) {
        hpBar = new HPBar(barX, barY, HPBar.INDENT, maxWidth, HPBar.BAR_HEIGHT);
        this.imgId = imgId;
        this.classId = imgId;
        this.id = id;
        this.hand = new Hand();
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
            //int nameLen2 = g.getFontMetrics().stringWidth(name + " (id: " + id + ")") / 2;

            final String name1 = name;
            final String name2 = name + " (id: " + id + ") + x/y: " + x + "/" + y + " : " + instance.getPlayer().getId() + " " + CLASS_NAMES[classId];
            g.drawString(Main.ENABLE_DEBUG_TOOLS ? name2 : name1,
                    xo + 16 - g.getFontMetrics().stringWidth(Main.ENABLE_DEBUG_TOOLS ? name2 : name1) / 2, yo - 8);
        }
    }

    public void setImage(int imgId) {
        try {
            this.setSprite(ImageIO.read(getClass().getResourceAsStream("/res/sprites/hero" + imgId + ".png")));
            this.imgId = imgId;
            this.classId = imgId;
        } catch (IOException ignored) {
        }
    }

    /**
     * @param yAdd
     * @deprecated @param xAdd
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

    public void subtractActionPoints(float cost) {
        actionPoints -= cost;
    }

    public void decreaseBloodCostHP(int cost) {
        hp = (hp - cost < 0) ? 0 : (hp - cost);
    }

    public void resetActionPoints() {
        actionPoints = actionPoints > 0 ? 10 + 1 : 10;
    }

    public void dealPhysicalDamage(int amount) {
        Main.addToChat("Got " + (amount - pDef) + " damage\r\n");
        hp = (hp - (amount - pDef) < 0) ? 0 : (hp - (amount - pDef));
    }

    public void heal(int power) {
        hp = (hp + power <= maxHp) ? hp + power : maxHp;
    }


    public void addAPInNextTurn(int value) {
        actionPoints += value;
    }

    public void drawCards(int value) {
        for (int i = 0; i < value; i++) {
            Card card = CardsArchive.Companion.getInstance().getRandomCard();
            hand.addCard(card.toDrawableCard());
            Core_v1.getInstance().incrementCardsDrawn();
        }
    }

    public void undrawCards(int value) {
        for (int i = 0; i < value; i++) {
            hand.removeLastCard();
        }
    }

    public void applyEffects(Card card, Entity target) {
        card.getEffects().forEach(effect -> {
            Card.Companion.EffectType type = effect.getEffectType();
            int value = effect.getValue();

            switch (type) {
                case DAMAGE:
                    target.dealPhysicalDamage(value);
                    break;
                case DRAW_CARD:
                    drawCards(value);
                    break;
                case UNDRAW_CARD:
                    target.undrawCards(value);
                    break;
                case HEAL:
                    target.heal(value);
                    break;
                case ADD_AP:
                    target.addAPInNextTurn(value);
                    break;
                case BLOODCOST:
                    decreaseBloodCostHP(value);
                    break;
                default:
                    Main.addToChat("BUT NOTHING HAPPENED\r\n");
                    break;
            }
        });
    }


}
