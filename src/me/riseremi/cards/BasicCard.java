package me.riseremi.cards;

import me.riseremi.core.Core_v1;
import me.riseremi.entities.Entity;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Riseremi
 */
public final class BasicCard {

    @Getter private final int id;
    @Getter private final String name;
    @Getter @Setter private Effect effect;
    @Getter private final Type type;
    @Getter private final int power;
    @Getter private final int cost;
    @Getter private final int bloodCost;
    @Getter private final int useRadius;
    @Getter private BufferedImage bigCard, smallCard;
    @Getter @Setter private Rectangle rect;
    @Getter @Setter private boolean paintBig = false;
    public static final int TEST_MAGIC_STRONG_ID = 1, TEST_PHYS_ID = 2,
            TEST_MAGIC_WEAK_ID = 3, BLINK_ID = 4, HEAL_ID = 6, ADD_AP_ID = 7, TEST_BLOOD1 = 8;
    public static final int WIDTH = 42, HEIGHT = 60;

    public BasicCard(int id, String pathToBigCard, String name, Effect effect,
            Type type, int power, int cost, int bloodCost, int useRadius) {
        try {
            this.bigCard = ImageIO.read(getClass().getResourceAsStream(pathToBigCard));
            this.smallCard = scaleImage(bigCard, WIDTH, HEIGHT);
        } catch (IOException ex) {
        }
        this.id = id;
        this.name = name;
        this.effect = effect;
        this.type = type;
        this.power = power;
        this.cost = cost;
        this.bloodCost = bloodCost;
        this.useRadius = useRadius;
        this.rect = new Rectangle();
    }

    public BasicCard(int id, BufferedImage bigCard, String name, Effect effect,
            Type type, int power, int cost, int bloodCost, int useRadius) {
        this.id = id;
        this.bigCard = bigCard;
        this.smallCard = scaleImage(bigCard, WIDTH, HEIGHT);
        this.name = name;
        this.effect = effect;
        this.type = type;
        this.power = power;
        this.cost = cost;
        this.bloodCost = bloodCost;
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
            switchIt(effect, user, target);
        } else if (justApplyEffect) {
            switchIt(effect, user, target);
        }
    }

    public void applyEffectFromTo(Entity user, Entity targetI) {
         use(user, targetI, true);
    }

    private void switchIt(Effect effect, Entity user, Entity target) {
        switch (effect) {
            case PHYSICAL_DAMAGE:
                target.dealPhysicalDamage(power);
                break;
            case MAGICAL_DAMAGE:
                target.dealMagicalDamage(power);
                break;
            case BLINK:
                break;
            case HEAL:
                target.heal(power);
                break;
            case AP:
                target.addAPInNextTurn(power);
                break;
            case BLOODY:
                target.dealMagicalDamage(power);
                user.decreaseBloodCostHP(bloodCost);
                break;
        }
    }

    public enum Effect {

        MAGICAL_DAMAGE, PHYSICAL_DAMAGE, HEAL, BLINK, WAIT, AP, BLOODY, NONE
    }

    public enum Type {

        PHYS, MAGI, NONE
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
        return new BasicCard(id, bigCard, name, effect, type, power, cost, bloodCost, useRadius);
    }

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

}
