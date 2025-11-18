package com.example.tarot.domain

import com.example.tarot.data.CardMetadata
import com.example.tarot.data.SpreadType

sealed class ReadingError(message: String) : Throwable(message) {
    class InvalidSpreadSize : ReadingError("Card selection does not match the spread requirements.")
    class UnknownCard : ReadingError("One or more selected cards are not available in the catalog.")
}

data class ReadingRequest(
    val question: String,
    val spreadType: SpreadType,
    val cardIds: List<String>
)

data class ReadingSection(
    val title: String,
    val body: String
)

data class TarotReading(
    val archetypalImage: ReadingSection,
    val psychologicalInterpretation: ReadingSection,
    val mirror: ReadingSection,
    val pathToIndividuation: ReadingSection,
    val cardDetails: List<CardMetadata>
)
