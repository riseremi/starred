package me.riseremi.utils

import java.awt.RenderingHints
import java.awt.image.BufferedImage

/**
 * by riseremi on 15.09.17
 * riseremi@icloud.com
 */
fun scaleImage(img: BufferedImage, width: Int, height: Int): BufferedImage {
    var scaledWidth = width
    var scaledHeight = height

    if (img.width * height < img.width * width) {
        scaledWidth = img.width * height / img.width
    } else {
        scaledHeight = img.width * width / img.width
    }
    val scaledImage = BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB)
    val g = scaledImage.createGraphics()
    try {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        g.drawImage(img, 0, 0, scaledWidth, scaledHeight, null)
    } finally {
        g.dispose()
    }
    return scaledImage
}
