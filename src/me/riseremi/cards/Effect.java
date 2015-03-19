package me.riseremi.cards;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Remi Weiss <riseremi at icloud.com>
 */
public class Effect {

    @Getter @Setter private BasicCard.EffectType effectType;
    @Getter @Setter private Object value;

    public Effect() {
        this.effectType = BasicCard.EffectType.NONE;
        this.value = 0;
    }

    @Override
    public String toString() {
        return effectType.name() + " = " + value.toString();
    }

    
}
