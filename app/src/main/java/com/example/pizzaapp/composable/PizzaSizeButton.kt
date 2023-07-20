package com.example.pizzaapp.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PizzaSizeButton(
    label: String,
    size: Float,
    isSelected: Boolean,
    onClick: (Float) -> Unit
) {
    val colors = if (isSelected) ButtonDefaults.buttonColors(Color.Gray) else ButtonDefaults.buttonColors(Color.LightGray)
    Button(
        colors = colors,
        onClick = { onClick(size) }
    ) {
        Text(
            text = label,
            modifier = Modifier
                .clip(CircleShape)
                .padding(8.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}