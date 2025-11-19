package com.example.tarot.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.tarot.data.SpreadType
import com.example.tarot.ui.theme.TarotPalette

@Composable
fun SpreadSelector(
    selected: SpreadType,
    onSelect: (SpreadType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SpreadType.values().forEach { spread ->
            val isSelected = spread == selected
            val background = if (isSelected) TarotPalette.accent else TarotPalette.surface
            val content = if (isSelected) MaterialTheme.colorScheme.onPrimary else TarotPalette.onSurface
            Text(
                text = spread.displayName(),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .border(1.dp, TarotPalette.muted, MaterialTheme.shapes.small)
                    .clickable { onSelect(spread) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                color = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun SpreadType.displayName(): String {
    return when (this) {
        SpreadType.ONE_CARD -> "1 Carta"
        SpreadType.TWO_CARD -> "2 Cartas"
        SpreadType.THREE_CARD -> "3 Cartas"
    }
}
