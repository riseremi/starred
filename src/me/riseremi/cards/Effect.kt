package me.riseremi.cards

/**
 *
 * @author riseremi <riseremi at icloud.com>
</riseremi> */
class Effect(private val effectType: Card.Companion.EffectType, private val value: Int) {

    override fun toString(): String {
        return effectType.name + " = " + value.toString()
    }

    // NOTE: for compatibility only
    // TODO: remove in flavor of new Card class
    fun getEffectType(): Card.Companion.EffectType {
        return effectType
    }

    // NOTE: for compatibility only
    // TODO: remove in flavor of new Card class
    fun getValue(): Int {
        return value
    }
}
