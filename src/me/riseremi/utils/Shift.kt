package me.riseremi.utils

/**
 *
 * @author riseremi <riseremi at icloud.com>
</riseremi> */
class Shift {
    companion object {
        fun ShiftNorth(p: Int, distance: Int): Int {
            return p - distance
        }

        fun ShiftSouth(p: Int, distance: Int): Int {
            return p + distance
        }

        fun ShiftEast(p: Int, distance: Int): Int {
            return p + distance
        }

        fun ShiftWest(p: Int, distance: Int): Int {
            return p - distance
        }
    }

}
