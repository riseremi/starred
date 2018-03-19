package me.riseremi.cards

import me.riseremi.utils.Shift
import me.riseremi.utils.scaleImage
import me.riseremi.utils.splitInChunks
import java.awt.Color
import java.awt.Font
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
        cover = createCover(card.frame, card.art, card.description, card.name)
        preview = createPreview(cover)
    }

    companion object {
        const val PREVIEW_WIDTH: Int = 42
        const val PREVIEW_HEIGHT: Int = 60
    }

    fun toCard(): Card {
        return card
    }

    private fun createPreview(image: BufferedImage): BufferedImage {
        return scaleImage(image, PREVIEW_WIDTH, PREVIEW_HEIGHT)
    }

    private fun createCover(appearance: BufferedImage, art: BufferedImage, description: String, name: String): BufferedImage {
        return buildBigCard(appearance, art, description, name)
    }

    private fun buildBigCard(img: BufferedImage, art: BufferedImage, description: String, name: String): BufferedImage {
        val strings = splitInChunks(30, description)

        val newImage = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)
        val g = newImage.createGraphics()

        g.drawImage(img, null, 0, 0)
        g.drawImage(art, null, 36, 36)

        g.color = Color.RED
        g.font = Font("Arial", Font.BOLD, 28)

        //determine X position to center the title
        val cardWidth = img.width
        var titleWidth = g.fontMetrics.stringWidth(name)

        if (titleWidth > cardWidth - 32) {
            g.font = Font("Arial", Font.BOLD, 18)
            titleWidth = g.fontMetrics.stringWidth(name)
        }

        val xPosition = cardWidth / 2 - titleWidth / 2
        val yPosition = 32
        val shiftValue = 2

        //outline
        g.color = Color.BLACK
        g.drawString(name, Shift.ShiftWest(xPosition, shiftValue), Shift.ShiftNorth(yPosition, shiftValue))
        g.drawString(name, Shift.ShiftWest(xPosition, shiftValue), Shift.ShiftSouth(yPosition, shiftValue))
        g.drawString(name, Shift.ShiftEast(xPosition, shiftValue), Shift.ShiftNorth(yPosition, shiftValue))
        g.drawString(name, Shift.ShiftEast(xPosition, shiftValue), Shift.ShiftSouth(yPosition, shiftValue))

        //actual title
        g.color = Color.WHITE
        g.drawString(name, xPosition, 32)

        g.font = Font("Arial", Font.PLAIN, 12)

        g.color = Color.WHITE

        for (j in strings.indices) {
            g.drawString(strings[j], 40, 305 + j * 14)
        }

        return newImage
    }
}
