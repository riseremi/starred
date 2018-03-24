package me.riseremi.cards

import me.riseremi.main.Main
import me.riseremi.utils.Shift
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Rectangle
import java.util.*

/**
 * Колода карт, которые находятся на руках у героя.
 *
 * @author riseremi <riseremi at icloud.com>
</riseremi> */
class Hand {
    private val cards = ArrayList<DrawableCard>()

    val activeCard: DrawableCard?
        get() {
            for (card in cards) {
                if (card.hover) {
                    return card
                }
            }
            return null
        }

    fun paint(g: Graphics2D) {
        for (i in cards.indices) {
            val x = Main.getFrames()[0].width - DrawableCard.PREVIEW_WIDTH - 12
            var y = 32 + i * (DrawableCard.PREVIEW_HEIGHT + 4)
            val card = cards[i]

            val paintRect = Rectangle(x, y, DrawableCard.PREVIEW_WIDTH, DrawableCard.PREVIEW_HEIGHT)
            card.collisionRectangle = paintRect

            g.drawImage(card.preview, x, y, null)
            g.drawRect(paintRect.x, paintRect.y, paintRect.width, paintRect.height)
            g.drawRect(x, y, DrawableCard.PREVIEW_WIDTH, DrawableCard.PREVIEW_HEIGHT)

            val trb = Font("Arial", Font.BOLD, 28)
            g.font = trb

            y += DrawableCard.PREVIEW_HEIGHT / 3

            g.color = Color.BLACK
            g.drawString("" + card.card.apcost, Shift.shiftWest(x, 1), Shift.shiftNorth(y, 1))
            g.drawString("" + card.card.apcost, Shift.shiftWest(x, 1), Shift.shiftSouth(y, 1))
            g.drawString("" + card.card.apcost, Shift.shiftEast(x, 1), Shift.shiftNorth(y, 1))
            g.drawString("" + card.card.apcost, Shift.shiftEast(x, 1), Shift.shiftSouth(y, 1))

            //red color for cards with blood cost
            g.color = Color.WHITE
            g.drawString("" + card.card.apcost, x, y)

            g.color = Color.WHITE

            //draw a big card cover
            if (card.hover) {
                g.drawImage(card.cover, 16, 24, null)
            }
        }
    }

    fun getCard(slot: Int): DrawableCard {
        return cards[slot]
    }

    fun getCards(): List<DrawableCard> {
        return cards
    }

    fun addCard(card: DrawableCard) {
        if (cards.size < SIZE) {
            cards.add(card)
        }
    }

    fun removeCard(card: DrawableCard) {
        cards.remove(card)
    }

    fun removeLastCard() {
        if (cards.size > 0) {
            cards.removeAt(cards.size - 1)
        }
    }

    fun size(): Int {
        return cards.size
    }

    fun switchPaint(mouseRect: Rectangle) {
        for (card in this.cards) {
            card.hover = mouseRect.intersects(card.collisionRectangle)
            if (card.hover) return
        }
    }

    companion object {
        const val SIZE = 8
    }
}
