package me.riseremi.map.world;

/**
 *
 * @author remi
 */
public class Room {

    private int num, x, y, w, h;

    public Room(int num, int x, int y, int w, int h) {
        this.num = num;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
