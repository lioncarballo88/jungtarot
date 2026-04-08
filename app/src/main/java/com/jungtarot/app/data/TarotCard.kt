package com.jungtarot.app.data

enum class ArcanaType { MAJOR, MINOR }

enum class SpreadType(val cardCount: Int) {
    LA_CRUZ(3),
    LA_CRUZ_CELTA(5),
    LA_HERRADURA(7);

    fun displayName(): String = when (this) {
        LA_CRUZ -> "La Cruz"
        LA_CRUZ_CELTA -> "Cruz Celta"
        LA_HERRADURA -> "La Herradura"
    }

    fun description(): String = when (this) {
        LA_CRUZ -> "Pasado · Presente · Futuro"
        LA_CRUZ_CELTA -> "Situación · Obstáculo · Pasado · Futuro · Resultado"
        LA_HERRADURA -> "7 cartas · Lectura completa"
    }
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
