package me.riseremi.cards

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * by riseremi on 14.09.17
 * riseremi@icloud.com
 */
class ImageDrawer {
    fun getImage(): BufferedImage? {
        return ImageIO.read(ImageDrawer::class.java.getResourceAsStream("/res/cards/backs/card_blood.png"))
    }
}
