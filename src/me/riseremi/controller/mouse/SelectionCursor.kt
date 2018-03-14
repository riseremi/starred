package me.riseremi.controller.mouse

import me.riseremi.core.Core_v1
import me.riseremi.core.Global
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * Rectangle cursor that appears only when you use a card (tile/target
 * selection).
 *
 * @author riseremi <riseremi at icloud.com>
</riseremi> */
class SelectionCursor(core: Core_v1) {
    private val core: Core_v1
    private val rectangle: Rectangle = Rectangle()
    var realX: Int = 0
    var realY: Int = 0
    private val color = Color(231, 76, 60)

    val x: Int get() = rectangle.x
    val y: Int get() = rectangle.y

    init {
        rectangle.width = Global.tileWidth
        rectangle.height = Global.tileHeight
        this.core = core
    }

    private fun updateRealPosition() {
        realX = (rectangle.x - core.camera.x) / Global.tileWidth
        realY = (rectangle.y - core.camera.y) / Global.tileHeight
    }

    fun setPosition(blockX: Int, blockY: Int) {
        rectangle.x = blockX
        rectangle.y = blockY
    }

    fun paint(g: Graphics) {
        updateRealPosition()
        g.color = color
        g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
    }

}
