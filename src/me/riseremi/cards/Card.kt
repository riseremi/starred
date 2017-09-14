package me.riseremi.cards

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
        //        val appearance: BufferedImage,
        val appearancePath: String,
        //        val art: BufferedImage,
        val artPath: String,
        val apcost: Int,
        val range: IntArray,
        val effects: List<Effect>
) {
    private constructor(builder: Builder) : this(
            builder.id,
            builder.name,
            builder.description,
            builder.type,
            builder.appearancePath,
            builder.artPath,
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
        var appearancePath: String = ""
        var artPath: String = ""
        var apcost: Int = 0
        var range: IntArray = intArrayOf(0, 0)
        var effects: List<Effect> = listOf()
        var appearance: BufferedImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
        var art: BufferedImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)

        fun build() = Card(this)
    }
}