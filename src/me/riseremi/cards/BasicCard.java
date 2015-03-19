package me.riseremi.cards;

import java.awt.Color;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Riseremi
 */
public final class BasicCard {

    private BufferedImage back, art;
    //private String description;
    //
    @Getter
    private final int id;
    @Getter
    private final String name;
//    @Getter @Setter private EffectType effect;
    @Getter
    private final Type type;
    @Getter
    private final int cost;
//    @Getter
//    private final int bloodCost;
    @Getter
    private final int useRadius;
    @Getter
    private BufferedImage bigCard, smallCard;
    @Getter
    @Setter
    private Rectangle rect;
    @Getter
    @Setter
    private boolean paintBig = false;
    public static final int //
            MAGICAL_SLAIN = 1,
            SWORD_ATTACK = 2,
            FIREBALL = 3,
            BLINK = 4,
            HEAL_ID = 6,
            ADD_AP_ID = 7,
            TEST_BLOOD1 = 8,
            TEST_TI4 = 9;
    //
    @Getter
    @Setter
    //private EffectType[] effects;
    //    private HashMap<String, Object> effectsMap;
    private Effect[] effects;

    public static final int WIDTH = 42, HEIGHT = 60;

    public BasicCard(String description, int id, String pathToBigCard, String pathToArt,
            String name, Effect[] effects, Type type, int cost,
            int useRadius) {
        try {
            this.art = ImageIO.read(getClass().getResourceAsStream(pathToArt));
            this.bigCard = ImageIO.read(getClass().getResourceAsStream(pathToBigCard));
            this.smallCard = scaleImage(bigCard, WIDTH, HEIGHT);
        } catch (IOException ex) {
        }
        this.id = id;
        this.name = name;
        this.effects = effects;
        this.type = type;
//        this.power = power;
        this.cost = cost;
//        this.bloodCost = bloodCost;
        this.useRadius = useRadius;
        this.rect = new Rectangle();

        this.bigCard = wwTest(bigCard, art, description);
    }

    public BasicCard(int id, BufferedImage bigCard, String name,
            Effect[] effects, Type type, int cost, int useRadius) {
        this.id = id;
        this.bigCard = bigCard;
        this.smallCard = scaleImage(bigCard, WIDTH, HEIGHT);
        this.name = name;
        this.effects = effects;
        this.type = type;
//        this.power = power;
        this.cost = cost;
//        this.bloodCost = bloodCost;
        this.useRadius = useRadius;
        this.rect = new Rectangle();
    }

    public void useByTo(Entity player, Entity friend) {
        final Core_v1 core = Core_v1.getInstance();
        if (player.canDoIt(cost)) {

            player.setCanMove(false);
            core.setTileSelectionMode(true);
            player.getDeck().setJustUsedCard(this);
        }
    }

    public void use(Entity user, Entity target, boolean justApplyEffect) {
        if (user.canDoIt(cost) && !justApplyEffect) {
            user.decreaseActionPoint(cost);
            switchIt(effects, user, target);
        } else if (justApplyEffect) {
            switchIt(effects, user, target);
        }
    }

    public void applyEffectFromTo(Entity user, Entity targetI) {
        use(user, targetI, true);
    }

    private void switchIt(Effect[] effects, Entity user, Entity target) {
        for (Effect effect1 : effects) {
            EffectType effect = effect1.getEffectType();
            int value = Integer.parseInt((String) effect1.getValue());
            switch (effect) {
                case PDMG:
                    target.dealPhysicalDamage(value);
                    break;
                case MDMG:
                    target.dealMagicalDamage(value);
                    break;
                case BLINK:
                    break;
                case HEAL:
                    target.heal(value);
                    break;
                case AP:
                    target.addAPInNextTurn(value);
                    break;
                case BLOODDMG:
                    target.dealMagicalDamage(value);
                    break;
                case BLOODCOST:
                    user.decreaseBloodCostHP(value);
                    break;
            }
        }
    }

    public enum EffectType {

        MDMG, PDMG, HEAL, BLINK, WAIT, AP, BLOODDMG, BLOODCOST, NONE
    }

    public enum Type {

        PHYS, MAGIC, NONE
    }

    public int getWidth() {
        return (int) rect.getWidth();
    }

    public int getHeight() {
        return (int) rect.getHeight();
    }

    public int getX() {
        return (int) rect.getX();
    }

    public int getY() {
        return (int) rect.getY();
    }

    @Override
    protected BasicCard clone() throws CloneNotSupportedException {
        return new BasicCard(id, bigCard, name, effects, type, cost, useRadius);
    }

    //uses for card preview
    private BufferedImage scaleImage(BufferedImage img, int width, int height) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        if (imgWidth * height < imgHeight * width) {
            width = imgWidth * height / imgHeight;
        } else {
            height = imgHeight * width / imgWidth;
        }
        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            //g.setBackground(background);
            //g.clearRect(0, 0, width, height);
            g.drawImage(img, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        return newImage;
    }

    private BufferedImage wwTest(BufferedImage img, BufferedImage art, String s) {
        StringBuilder sb = new StringBuilder(s);

        int i = 0;
        while (i + 30 < sb.length() && (i = sb.lastIndexOf(" ", i + 30)) != -1) {
            sb.replace(i, i + 1, "\n");
        }

        //System.out.println(sb.toString());

        String[] strings = sb.toString().split("\n");

        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();

        g.drawImage(img, null, 0, 0);
        g.drawImage(art, null, 36, 36);

        g.setColor(Color.WHITE);
        //g.drawString(sb.toString(), 40, 300);

        for (int j = 0; j < strings.length; j++) {
            g.drawString(strings[j], 40, 305 + j * 14);
        }

        return newImage;

    }

}
