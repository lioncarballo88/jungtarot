package com.jungtarot.app.domain

import com.jungtarot.app.data.CardMetadata
import com.jungtarot.app.data.SpreadType

sealed class ReadingError(message: String) : Throwable(message) {
    class InvalidSpreadSize : ReadingError("La selección de cartas no coincide con la tirada.")
    class UnknownCard : ReadingError("Una o más cartas no están disponibles en el mazo.")
}

data class ReadingRequest(
    val spreadType: SpreadType,
    val cardIds: List<String>
)

data class TarotReading(
    val response: String,
    val cardDetails: List<CardMetadata>
)
