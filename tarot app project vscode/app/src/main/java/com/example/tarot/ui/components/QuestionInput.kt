package com.example.tarot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tarot.ui.theme.TarotPalette

@Composable
fun QuestionInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .background(TarotPalette.surface, RoundedCornerShape(20.dp)),
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = { Text("Formula tu pregunta...", color = TarotPalette.muted) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = TarotPalette.surface,
            unfocusedContainerColor = TarotPalette.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = TarotPalette.accent
        )
    )
}
