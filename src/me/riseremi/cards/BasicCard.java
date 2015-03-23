package me.riseremi.cards;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.Setter;
import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import me.riseremi.main.Main;
import me.riseremi.utils.Shift;

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
            HEALING_WORD = 5,
            BLOOD_CRUSH = 6,
            ADD_AP_ID = 7,
            SACRIFICE = 8,
            GREATER_HEAL = 9,
            BLOOD_RITUAL = 10;
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

        this.bigCard = wwTest(bigCard, art, description, name, id);
    }

    public BasicCard(int id, BufferedImage bigCard, String name,
            Effect[] effects, Type type, int cost, int useRadius) {
        this.id = id;
        this.bigCard = bigCard;
        this.smallCard = scaleImage(bigCard, WIDTH, HEIGHT);
        this.name = name;
        this.effects = effects;
        this.type = type;
        this.cost = cost;
        this.useRadius = useRadius;
        this.rect = new Rectangle();
    }

    public void setAsSelectedCard(Entity player, Entity friend) {
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

    @Deprecated
    public void applyEffectFromTo(Entity user, Entity targetI) {
        use(user, targetI, true);
    }

    /* use card */
    private void switchIt(Effect[] effects, Entity user, Entity target) {
        for (Effect effect1 : effects) {
            EffectType effect = effect1.getEffectType();
            int value = Integer.parseInt((String) effect1.getValue());
            switch (effect) {
                case DAMAGE:
                    target.dealPhysicalDamage(value);
                    break;
                case BLINK:
                    break;
                case BLINK_OPPONENT:
                    break;
                case HEAL:
                    target.heal(value);
                    break;
                case ADD_AP:
                    target.addAPInNextTurn(value);
                    break;
                case BLOODCOST:
                    user.decreaseBloodCostHP(value);
                    break;
                case NONE:
                    Main.addToChat("BUT NOTHING HAPPENS\r\n");
                    break;
            }
        }
    }

    public enum EffectType {

        DAMAGE, HEAL, BLINK, WAIT, ADD_AP, BLOODCOST, BLINK_OPPONENT, NONE
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

    /**
     * Returns a blood cost value if one found.
     *
     * @return blood cost value
     */
    public int getBloodCost() {
        for (Effect effect : effects) {
            if (effect.getEffectType() == EffectType.BLOODCOST) {
                return Integer.parseInt(((String) effect.getValue()));
            }
        }
        return 0;
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

    private BufferedImage wwTest(BufferedImage img, BufferedImage art, String s, String name, int id) {
        //s = s.replace("|", "\n");
        StringBuilder sb = new StringBuilder(s);

        int i = 0;
        while (i + 30 < sb.length() && (i = sb.lastIndexOf(" ", i + 30)) != -1) {
            sb.replace(i, i + 1, "\n");
        }

        //System.out.println(sb.toString());
        String temp = sb.toString().replace("|", "\n").replace("=", ": ");
        temp = temp.replace("_", " ");
        String[] strings = temp.split("\n");

        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();

        g.drawImage(img, null, 0, 0);
        g.drawImage(art, null, 36, 36);

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 28));

        //determine X position to center the title
        int cardWidth = img.getWidth();
        int titleWidth = g.getFontMetrics().stringWidth(name);
        int xPosition = cardWidth / 2 - titleWidth / 2;
        int yPosition = 32;
        int shiftValue = 2;
        
        
        //outline
        g.setColor(Color.BLACK);
        g.drawString(name, Shift.ShiftWest(xPosition, shiftValue), Shift.ShiftNorth(yPosition, shiftValue));
        g.drawString(name, Shift.ShiftWest(xPosition, shiftValue), Shift.ShiftSouth(yPosition, shiftValue));
        g.drawString(name, Shift.ShiftEast(xPosition, shiftValue), Shift.ShiftNorth(yPosition, shiftValue));
        g.drawString(name, Shift.ShiftEast(xPosition, shiftValue), Shift.ShiftSouth(yPosition, shiftValue));

        //actual title
        g.setColor(Color.WHITE);
        g.drawString(name, xPosition, 32);

        g.setFont(new Font("Arial", Font.PLAIN, 12));

        g.setColor(Color.WHITE);

        for (int j = 0; j < strings.length; j++) {
            g.drawString(strings[j], 40, 305 + j * 14);
        }
        if (Main.CARD_DUMP) {
            try {
                File card = new File("C:/cards-dump/0" + (id < 10 ? "0" + id : id) + ".png");

                card.delete();
                card.createNewFile();
                ImageIO.write(newImage, "png", card);

                System.out.println("Saved " + card.getAbsolutePath());
            } catch (IOException ex) {
                System.out.println("Error while saving card. " + ex.toString());
            }
        }

        return newImage;

    }

}
