package me.riseremi.cards

import me.riseremi.utils.loadImage
import java.awt.image.BufferedImage

/**
 * by riseremi on 14.09.17
 * riseremi@icloud.com
 */
class Card(
        val id: Int,
        val name: String,
        val description: String,
        val type: String,
        val frame: BufferedImage,
        val art: BufferedImage,
        val apcost: Int,
        val range: IntArray,
        val effects: List<Effect>
) {
    private constructor(builder: Builder) : this(
            builder.id,
            builder.name,
            builder.description,
            builder.type,
            loadImage(builder.framePath),
            loadImage(builder.artPath),
            builder.apcost,
            builder.range,
            builder.effects
    )

    override fun toString(): String {
        var maxRange = range[0]
        if (range.size > 1) {
            maxRange = range[1]
        }
        return "id=$id name=$name description=$description type=$type apcost=$apcost minRange=" + range[0] + " maxRange=$maxRange" + " effects=" + effects.toString()
    }

    fun toDrawableCard(): DrawableCard {
        return DrawableCard(this)
    }

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()

        // Card IDs.
        const val MAGICAL_SLAIN: Int = 1
        const val SWORD_ATTACK: Int = 2
        const val FIREBALL: Int = 3
        const val BLINK: Int = 4
        const val HEALING_WORD: Int = 5
        const val BLOOD_CRUSH: Int = 6
        const val ADD_AP_ID: Int = 7
        const val SACRIFICE: Int = 8
        const val GREATER_HEAL: Int = 9
        const val BLOOD_RITUAL: Int = 10

        enum class EffectType {
            DAMAGE, HEAL, BLINK, WAIT, ADD_AP, BLOODCOST, BLINK_OPPONENT,
            DRAW_CARD, UNDRAW_CARD,
            NONE
        }

    }

    class Builder {
        var id: Int = -1
        var name: String = ""
        var description: String = ""
        var type: String = ""
        var framePath: String = ""
        var artPath: String = ""
        var apcost: Int = 0
        var range: IntArray = intArrayOf(0, 0)
        var effects: List<Effect> = listOf()

        fun build() = Card(this)
    }
}