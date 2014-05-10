package me.riseremi.cards;

import java.util.Random;

/**
 *
 * @author Riseremi
 */
public class CardsArchive {

    static final Random rnd = new Random();
    //power, cost, bloodCost, radius
    static final BasicCard testPhysAttackOn7Damage = new BasicCard(BasicCard.TEST_PHYS_ID, "/res/card_physical.png", "Test P-Card (7)",
            BasicCard.Effect.PHYSICAL_DAMAGE, BasicCard.Type.PHYS, 7, 4, 0, 1);
    static final BasicCard testMagAttackOn17Damage = new BasicCard(BasicCard.TEST_MAGIC_STRONG_ID, "/res/card_magic.png", "Test M-Card (17)",
            BasicCard.Effect.MAGICAL_DAMAGE, BasicCard.Type.MAGI, 11, 8, 0, 2);
    static final BasicCard testMagAttackOn7Damage = new BasicCard(BasicCard.TEST_MAGIC_WEAK_ID, "/res/card_magic_weak.png", "Test M-Card (7)",
            BasicCard.Effect.MAGICAL_DAMAGE, BasicCard.Type.MAGI, 3, 4, 0, 3);
    static final BasicCard testBlink = new BasicCard(BasicCard.BLINK_ID, "/res/card_blink2.png", "Test B-Card",
            BasicCard.Effect.BLINK, BasicCard.Type.NONE, 0, 3, 0, 5);
    static final BasicCard testHeal = new BasicCard(BasicCard.HEAL_ID, "/res/card_heal.png", "Test H-Card",
            BasicCard.Effect.HEAL, BasicCard.Type.NONE, 5, 2, 0, 8);
    static final BasicCard testAPCard = new BasicCard(BasicCard.ADD_AP_ID, "/res/card_blink.png", "Test AP-Card",
            BasicCard.Effect.AP, BasicCard.Type.NONE, 5, 3, 0, 8);
    static final BasicCard testBlood1Card = new BasicCard(BasicCard.TEST_BLOOD1, "/res/card_blood1.png", "Test B1-Card",
            BasicCard.Effect.BLOODY, BasicCard.Type.NONE, 14, 8, 10, 4);

    @Otsylka
    public static BasicCard getRandomCard() throws CloneNotSupportedException {
        int id = rnd.nextInt(8) + 1;
        //int id = 4;
        return get(id);
    }

    public static BasicCard get(int id) throws CloneNotSupportedException {
        switch (id) {
            case BasicCard.TEST_PHYS_ID:
                return testPhysAttackOn7Damage.clone();
            case BasicCard.TEST_MAGIC_STRONG_ID:
                return testMagAttackOn17Damage.clone();
            case BasicCard.TEST_MAGIC_WEAK_ID:
                return testMagAttackOn7Damage.clone();
            case BasicCard.BLINK_ID:
                return testBlink.clone();
            case BasicCard.BLINK_ID + 1:
                return testBlink.clone();
            case BasicCard.HEAL_ID:
                return testHeal.clone();
            case BasicCard.ADD_AP_ID:
                return testAPCard.clone();
            case BasicCard.TEST_BLOOD1:
                return testBlood1Card.clone();
            default:
                return null;
        }
    }
}
