package me.riseremi.utils

import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.imageio.ImageIO

// All these methods aren't really null-safe

@Throws(IOException::class)
fun loadImage(resName: String): BufferedImage {
    return ImageIO.read(ClassLoader.getSystemResourceAsStream(resName))
}

fun loadUrl(resName: String): URL = ClassLoader.getSystemResource(resName)

fun loadStream(resName: String): InputStream = ClassLoader.getSystemResourceAsStream(resName)