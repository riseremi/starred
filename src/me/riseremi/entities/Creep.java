package me.riseremi.entities;

/**
 *
 * @author riseremi <riseremi at icloud.com>
 */
public class Creep extends Entity {

    //@Getter @Setter private boolean isServer;
    public Creep(String name, int imgId, int id, Entity.Type type) {
//        this.deck = new Deck();
//        this.actionPoints = 10;
//        this.hp = this.maxHp = 30;
//        this.pAtk = 0;
//        this.pDef = 0;
//        this.mDef = 0;
//        this.setPaint(true);
//        this.setName(name);
        super(name, imgId, id, 0, 256, 100, type);
        //setDrawHPBar(false);
//        try {
//            this.setSprite(ImageIO.read(getClass().getResourceAsStream(sprite_path)));
//        } catch (IOException ex) {
//            System.out.println("Error while loading hero sprite");
//        }
    }

}
