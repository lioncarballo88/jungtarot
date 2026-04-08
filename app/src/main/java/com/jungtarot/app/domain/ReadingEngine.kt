package com.jungtarot.app.domain

import com.jungtarot.app.data.TarotRepository

class ReadingEngine(
    private val repository: TarotRepository,
    private val geminiService: GeminiService = GeminiService(repository)
) {
    suspend fun run(request: ReadingRequest): Result<TarotReading> {
        if (request.cardIds.size != request.spreadType.cardCount) {
            return Result.failure(ReadingError.InvalidSpreadSize())
        }
        val metadata = repository.getCardMetadata(request.cardIds)
        if (metadata.size != request.cardIds.size) {
            return Result.failure(ReadingError.UnknownCard())
        }
        return try {
            val response = geminiService.generateReading(
                spreadType = request.spreadType,
                cards = metadata
            )
            Result.success(TarotReading(response = response, cardDetails = metadata))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
