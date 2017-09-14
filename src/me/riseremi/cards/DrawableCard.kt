package me.riseremi.cards

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * by riseremi on 14.09.17
 * riseremi@icloud.com
 */
class DrawableCard(var card: Card) {
    var hover: Boolean = false
    var cover: BufferedImage
    var preview: BufferedImage

    init {
        val appearanceImage = ImageIO.read(DrawableCard::class.java.getResourceAsStream(card.appearancePath))
        val artImage = ImageIO.read(DrawableCard::class.java.getResourceAsStream(card.artPath))
        this.cover = createCover(appearanceImage, artImage, "What is S?", card.name)
        this.preview = createPreview(cover)
    }

    fun getImage(): BufferedImage? {
        val appearanceImage = ImageIO.read(DrawableCard::class.java.getResourceAsStream(card.appearancePath))
        val artImage = ImageIO.read(DrawableCard::class.java.getResourceAsStream(card.artPath))
        cover = createCover(appearanceImage, artImage, "What is S?", card.name)
        return cover
    }

    private fun createPreview(image: BufferedImage): BufferedImage {
        return BasicCard.scaleImage(image, BasicCard.WIDTH, BasicCard.HEIGHT)
    }

    private fun createCover(appearance: BufferedImage, art: BufferedImage, s: String, name: String): BufferedImage {
        return BasicCard.buildBigCard(appearance, art, s, name)
    }
}
