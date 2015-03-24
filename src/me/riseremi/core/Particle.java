package me.riseremi.core;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author riseremi
 */
public class Particle {

    @Getter @Setter private Point speed = new Point();
    @Getter @Setter private double radius, life, remainingLife, opacity;
    @Getter @Setter private int r, g, b;
    private static final Random rnd = new Random();
    public int x, y;

    public Particle() {
        x = Core_v1.getInstance().getSelectionCursor().getRealX() * 32;
        y = Core_v1.getInstance().getSelectionCursor().getRealY() * 32;
        speed.x = (int) (-2.5 + Math.random() * 5);
        speed.y = (int) (-15 + Math.random() * 10);
        radius = 10 + Math.random() * 20;
        life = remainingLife = 20 + Math.random() * 10;
        r = rnd.nextInt(1) + 255;
        g = rnd.nextInt(1) + 25;
        b = rnd.nextInt(1) + 14;
    }

}
