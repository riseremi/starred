package me.riseremi.map.layer

import me.riseremi.core.Core_v1
import me.riseremi.core.Global
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.util.*

/**
 * @author LPzhelud
 * use of this class approved by the author 09.11.2012 - 9:15 AM
 */
class TiledLayer(image: BufferedImage, private val tileWidth: Int, private val tileHeight: Int, width: Int, height: Int) : Layer(width * tileWidth, height * tileHeight) {

    var map: Array<IntArray>? = null
    private val tiles: Array<BufferedImage>

    init {
        if (image.width / tileWidth * tileWidth != image.width || image.height / tileHeight * tileHeight != image.height) {
            throw IllegalArgumentException()
        }
        tiles = chopImage(image)
        map = Array(width) { IntArray(height) }

        for (i in 0 until width) {
            for (j in 0 until height) {
                map!![i][j] = 0
            }
        }
    }

    private fun chopImage(image: BufferedImage): Array<BufferedImage> {
        var x = 0
        var y = 0
        val list = ArrayList<BufferedImage>()
        while (true) {
            while (true) {
                val subImage = image.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight)
                list.add(subImage)
                x++
                if ((x + 1) * tileWidth > image.width) {
                    x = 0
                    break
                }
            }
            y++
            if ((y + 1) * tileHeight > image.height) {
                break
            }
        }

        return list.toTypedArray()
    }

    fun getTile(x: Int, y: Int): Int = map!![x][y]

    // отрисовка слоя, при этом рисуются только помещающиеся на экран тайлы
    fun paintLayer(g: Graphics) {
        val xStart = Math.abs(Core_v1.getInstance().camera.x / Global.tileWidth)
        val yStart = Math.abs(Core_v1.getInstance().camera.y / Global.tileHeight)
        // новая отрисовка
        for (i in xStart until Global.paintWidth + xStart) {
            for (j in yStart until Global.paintHeight + yStart) {
                paintTile(g, i * tileWidth, j * tileHeight, map!![i][j])
            }
        }
    }

    private fun paintTile(g: Graphics, x: Int, y: Int, id: Int) {
        g.drawImage(tiles[id], x, y, null)
        g.color = Color.white
    }
}
