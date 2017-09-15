package me.riseremi.cards

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

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
            ImageIO.read(Card::class.java.getResourceAsStream(builder.framePath)),
            ImageIO.read(Card::class.java.getResourceAsStream(builder.artPath)),
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