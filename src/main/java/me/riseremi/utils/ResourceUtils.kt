package me.riseremi.utils

import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

// All these methods aren't really null-safe

@Throws(IOException::class)
fun loadImage(resName: String): BufferedImage {
    return ImageIO.read(ClassLoader.getSystemResourceAsStream(resName))
}