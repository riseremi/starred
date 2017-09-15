package me.riseremi.cards

import java.awt.Rectangle
import java.awt.image.BufferedImage

/**
 * by riseremi on 14.09.17
 * riseremi@icloud.com
 */
class DrawableCard(var card: Card) {
    var hover: Boolean = false
    var cover: BufferedImage
    var preview: BufferedImage
    var collisionRectangle: Rectangle = Rectangle()

    init {
        cover = createCover(card.frame, card.art, "What is S?", card.name)
        preview = createPreview(cover)
    }

    private fun createPreview(image: BufferedImage): BufferedImage {
        return BasicCard.scaleImage(image, BasicCard.WIDTH, BasicCard.HEIGHT)
    }

    private fun createCover(appearance: BufferedImage, art: BufferedImage, s: String, name: String): BufferedImage {
        return BasicCard.buildBigCard(appearance, art, s, name)
    }
}
