package com.example.pizzaapp.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pizzaapp.screen.pizzaScreen.OrderUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PizzaSlider(
    modifier: Modifier = Modifier,
    state: OrderUiState,
    pagerState: PagerState,
    content: @Composable (page: Int) -> Unit
) {
    HorizontalPager(
        modifier = modifier,
        pageCount = state.pizzaBreads.size,
        state = pagerState,
        contentPadding = PaddingValues(start = 32.dp, end = 32.dp),
        pageSpacing = 16.dp
    ) { page ->
        content(page)
    }
}