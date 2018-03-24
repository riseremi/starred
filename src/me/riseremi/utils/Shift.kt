package me.riseremi.utils

/**
 *
 * @author riseremi <riseremi at icloud.com>
</riseremi> */
class Shift {
    companion object {
        fun shiftNorth(p: Int, distance: Int): Int = p - distance
        fun shiftSouth(p: Int, distance: Int): Int = p + distance
        fun shiftEast(p: Int, distance: Int): Int = p + distance
        fun shiftWest(p: Int, distance: Int): Int = p - distance
    }
}
