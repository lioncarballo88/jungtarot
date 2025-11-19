package com.example.tarot.data

enum class ArcanaType { MAJOR, MINOR }

enum class SpreadType(val cardCount: Int) {
    ONE_CARD(1),
    TWO_CARD(2),
    THREE_CARD(3)
}

enum class TarotSuit { WANDS, CUPS, SWORDS, COINS }

data class TarotCard(
    val id: String,
    val arcana: ArcanaType,
    val name: String,
    val number: Int? = null,
    val suit: TarotSuit? = null
)

data class CardMetadata(
    val card: TarotCard,
    val archetypalImage: String,
    val psychologicalMeaning: String
)
