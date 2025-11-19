package com.example.tarot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tarot.data.TarotCard
import com.example.tarot.ui.theme.TarotPalette

@Composable
fun CardPicker(
    selectedCards: List<TarotCard>,
    spreadLabel: String,
    onOpenPicker: () -> Unit,
    onCardRemoved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onOpenPicker() },
        shape = RoundedCornerShape(20.dp),
        color = TarotPalette.surface,
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Selecciona ${spreadLabel.lowercase()}:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TarotPalette.onSurface
                )
                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = TarotPalette.accent)
            }
            if (selectedCards.isEmpty()) {
                Text(
                    text = "No hay cartas seleccionadas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TarotPalette.muted
                )
            } else {
                selectedCards.forEachIndexed { index, card ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(TarotPalette.surface, RoundedCornerShape(12.dp))
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}. ${card.name}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TarotPalette.onSurface
                        )
                        IconButton(onClick = { onCardRemoved(card.id) }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Remover carta", tint = TarotPalette.accent)
                        }
                    }
                }
            }
        }
    }
}
