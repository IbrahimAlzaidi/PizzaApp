package com.example.pizzaapp.composable

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.pizzaapp.R

@Composable
fun Plate(modifier: Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.plate), contentDescription = "plate"
    )
}