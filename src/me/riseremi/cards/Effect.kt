package me.riseremi.cards

/**
 *
 * @author riseremi <riseremi at icloud.com>
</riseremi> */
class Effect(private val effectType: BasicCard.EffectType, private val value: Int) {

    override fun toString(): String {
        return effectType.name + " = " + value.toString()
    }

    // NOTE: for compatibility only
    // TODO: remove in flavor of new Card class
    fun getEffectType(): BasicCard.EffectType {
        return BasicCard.EffectType.NONE
    }

    // NOTE: for compatibility only
    // TODO: remove in flavor of new Card class
    fun getValue(): Int {
        return 0
    }
}
