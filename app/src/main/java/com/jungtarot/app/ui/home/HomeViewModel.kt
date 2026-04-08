package com.jungtarot.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jungtarot.app.data.SpreadType
import com.jungtarot.app.data.TarotCard
import com.jungtarot.app.data.TarotRepository
import com.jungtarot.app.domain.GeminiService
import com.jungtarot.app.domain.ReadingEngine
import com.jungtarot.app.domain.ReadingRequest
import com.jungtarot.app.domain.TarotReading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeEvent {
    data class ReadingReady(val reading: TarotReading) : HomeEvent()
    data class Error(val message: String) : HomeEvent()
}

data class HomeUiState(
    val spreadType: SpreadType = SpreadType.LA_CRUZ,
    val selectedCards: List<TarotCard> = emptyList(),
    val isLoading: Boolean = false,
    val event: HomeEvent? = null
) {
    val canRunReading: Boolean = selectedCards.size == spreadType.cardCount
}

class HomeViewModel(
    private val repository: TarotRepository = TarotRepository(),
    private val readingEngine: ReadingEngine = ReadingEngine(TarotRepository(), GeminiService())
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    val catalog: List<TarotCard> by lazy { repository.getAllCards() }

    var lastReading: TarotReading? = null
        private set

    fun onSpreadSelected(spread: SpreadType) {
        _state.value = _state.value.copy(
            spreadType = spread,
            selectedCards = emptyList(),
            event = null
        )
    }

    fun onCardSelected(cardId: String) {
        val target = catalog.firstOrNull { it.id == cardId } ?: return
        val current = _state.value
        if (current.selectedCards.any { it.id == cardId }) return
        if (current.selectedCards.size >= current.spreadType.cardCount) return
        _state.value = current.copy(selectedCards = current.selectedCards + target, event = null)
    }

    fun onCardRemoved(cardId: String) {
        _state.value = _state.value.copy(
            selectedCards = _state.value.selectedCards.filterNot { it.id == cardId },
            event = null
        )
    }

    fun onRandomSelection() {
        val count = _state.value.spreadType.cardCount
        val random = catalog.shuffled().take(count)
        _state.value = _state.value.copy(selectedCards = random, event = null)
    }

    fun onReadingConsumed() { _state.value = _state.value.copy(event = null) }

    fun runReading() {
        val snapshot = _state.value
        if (!snapshot.canRunReading || snapshot.isLoading) return
        _state.value = snapshot.copy(isLoading = true, event = null)
        viewModelScope.launch {
            val result = readingEngine.run(
                ReadingRequest(
                    spreadType = snapshot.spreadType,
                    cardIds = snapshot.selectedCards.map { it.id }
                )
            )
            val error = result.exceptionOrNull()
            _state.value = if (error != null) {
                snapshot.copy(isLoading = false, event = HomeEvent.Error(error.message ?: "Se produjo un error"))
            } else {
                result.getOrThrow().let { reading ->
                    lastReading = reading
                    snapshot.copy(isLoading = false, event = HomeEvent.ReadingReady(reading))
                }
            }
        }
    }
}
