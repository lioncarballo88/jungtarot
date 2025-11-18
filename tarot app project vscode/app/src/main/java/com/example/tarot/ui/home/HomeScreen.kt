package com.example.tarot.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tarot.data.SpreadType
import com.example.tarot.data.TarotCard
import com.example.tarot.ui.components.CardPicker
import com.example.tarot.ui.components.PrimaryButton
import com.example.tarot.ui.components.QuestionInput
import com.example.tarot.ui.components.SpreadSelector
import com.example.tarot.ui.theme.TarotPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    catalog: List<TarotCard>,
    onQuestionChange: (String) -> Unit,
    onSpreadSelected: (SpreadType) -> Unit,
    onCardSelected: (String) -> Unit,
    onCardRemoved: (String) -> Unit,
    onRunReading: () -> Unit,
    onLoadSample: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val deckDialog = remember { mutableStateOf(false) }
    if (deckDialog.value) {
        DeckPickerDialog(
            cards = catalog,
            onSelect = {
                onCardSelected(it)
                deckDialog.value = false
            },
            onDismiss = { deckDialog.value = false }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = TarotPalette.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Surface(
                tonalElevation = 4.dp,
                color = TarotPalette.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                ) {
                    Text("Tarot de Individuación", style = MaterialTheme.typography.headlineSmall, color = TarotPalette.accent)
                    Text("Explora la energía actual", style = MaterialTheme.typography.bodyMedium, color = TarotPalette.onSurface)
                }
            }
        }
    ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            SpreadSelector(selected = state.spreadType, onSelect = onSpreadSelected)
            QuestionInput(value = state.question, onValueChange = onQuestionChange)
            CardPicker(
                selectedCards = state.selectedCards,
                spreadLabel = state.spreadType.displayName(),
                onOpenPicker = { deckDialog.value = true },
                onCardRemoved = onCardRemoved
            )
            PrimaryButton(text = "Iniciar lectura", enabled = state.canRunReading, onClick = onRunReading)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mostrar ejemplo", style = MaterialTheme.typography.bodyMedium, color = TarotPalette.muted)
                TextButton(onClick = onLoadSample) {
                    Text("Cargar muestra", color = TarotPalette.accent)
                }
            }
        }
    }
}

@Composable
private fun DeckPickerDialog(cards: List<TarotCard>, onSelect: (String) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona una carta", color = TarotPalette.onSurface) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                cards.forEach { card ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(card.id) }
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(card.name, color = TarotPalette.onSurface)
                        Text(card.arcana.name, color = TarotPalette.muted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = TarotPalette.accent)
            }
        }
    )
}

private fun SpreadType.displayName(): String {
    return when (this) {
        SpreadType.ONE_CARD -> "1 carta"
        SpreadType.TWO_CARD -> "2 cartas"
        SpreadType.THREE_CARD -> "3 cartas"
    }
}
