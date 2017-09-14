package me.riseremi.cards

import java.util.*
import kotlin.NoSuchElementException

/**
 * by riseremi on 14.09.17
 * riseremi@icloud.com
 */
class CardsArchivev3 {
    private var cards: HashMap<Int, Card> = HashMap()
    private val random = Random()

    private object Holder {
        val INSTANCE = CardsArchivev3()
    }

    companion object {
        val instance: CardsArchivev3 by lazy { Holder.INSTANCE }
    }

    fun addCard(card: Card) {
        cards[card.id] = card
    }

    fun getRandomCard(): Card? {
        val randomIndex = random.nextInt(cards.size - 1)
        // TODO: add error reporting
        return cards[randomIndex] ?: throw NoSuchElementException("Requested random card not found in CardsArchive")
    }

    fun getCard(id: Int): Card {
        // TODO: add error reporting
        return cards[id] ?: throw NoSuchElementException("Requested card not found in CardsArchive.")
    }

    override fun toString(): String {
        return cards.toString()
    }
}
