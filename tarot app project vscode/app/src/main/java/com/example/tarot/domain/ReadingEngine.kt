package com.example.tarot.domain

import com.example.tarot.data.TarotRepository
import com.example.tarot.domain.interpretation.ReadingTemplate

class ReadingEngine(
    private val repository: TarotRepository,
    private val template: ReadingTemplate
) {
    fun run(request: ReadingRequest): Result<TarotReading> {
        validate(request).exceptionOrNull()?.let { error ->
            return Result.failure(error)
        }

        val metadata = repository.getCardMetadata(request.cardIds)
        if (metadata.size != request.cardIds.size) {
            return Result.failure(ReadingError.UnknownCard())
        }

        val reading = template.buildReading(
            question = request.question,
            spreadType = request.spreadType,
            cards = metadata
        )
        return Result.success(reading)
    }

    private fun validate(request: ReadingRequest): Result<Unit> {
        return when {
            request.question.isBlank() -> Result.failure(
                IllegalArgumentException("The question cannot be empty.")
            )
            request.cardIds.size != request.spreadType.cardCount -> Result.failure(
                ReadingError.InvalidSpreadSize()
            )
            else -> Result.success(Unit)
        }
    }
}
