package com.example.pizzaapp.screen.pizzaScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pizzaapp.R
import com.example.pizzaapp.composable.CustomAppBar
import com.example.pizzaapp.composable.PizzaSizeButton
import com.example.pizzaapp.composable.PizzaSlider
import com.example.pizzaapp.composable.Plate
import com.example.pizzaapp.composable.SpacerVertical

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PizzaScreen(
    viewModel: PizzaViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState()
    PizzaContent(state, pagerState, viewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PizzaContent(
    state: OrderUiState,
    pagerState: PagerState,
    orderInteraction: PizzaInteractionListener,
) {
    Column(Modifier.fillMaxSize()) {
        CustomAppBar(Modifier.padding(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            contentAlignment = Alignment.Center
        ) {

            Plate(modifier = Modifier.size(256.dp))
            PizzaSlider(
                state = state,
                pagerState = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                Box {
                    Image(
                        modifier = Modifier.size(state.pizzaBreads[page].defaultSize.dp),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = state.pizzaBreads[page].image),
                        contentDescription = "pizza"
                    )
                }

                state.pizzaBreads[pagerState.currentPage].pizzaIngredients.forEach {
                    IngredientsPizzaAnimation(it)
                }
            }
        }
        SpacerVertical(8)
        Text(
            text = "10$",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        SpacerVertical(16)
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PizzaSizeButton(
                label = "S",
                size = 160f,
                isSelected = state.pizzaBreads[pagerState.currentPage].defaultSize == 160f,
                onClick = { size ->
                    orderInteraction.onChangePizzaSize(
                        pagerState.currentPage,
                        size
                    )
                }
            )
            PizzaSizeButton(
                label = "M",
                size = 200f,
                isSelected = state.pizzaBreads[pagerState.currentPage].defaultSize == 200f,
                onClick = { size ->
                    orderInteraction.onChangePizzaSize(
                        pagerState.currentPage,
                        size
                    )
                }
            )
            PizzaSizeButton(
                label = "L",
                size = 230f,
                isSelected = state.pizzaBreads[pagerState.currentPage].defaultSize == 230f,
                onClick = { size ->
                    orderInteraction.onChangePizzaSize(
                        pagerState.currentPage,
                        size
                    )
                }
            )
        }

        SpacerVertical(16)
        Text(
            text = stringResource(R.string.customize_your_pizza),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        SpacerVertical(16)
        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(state.pizzaBreads[pagerState.currentPage].pizzaIngredients) { index: Int, item: IngredientsPizzaTypeUiState ->
                PizzaIngredients(state = item,
                    isSelected = item.isSelected,
                    onClick = {
                        orderInteraction.onIngredientsPizzaClick(
                            index,
                            pagerState.currentPage
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
// Constants
private const val INITIAL_SCALE = 2f
private const val ROTATION_ANGLE = 360f
private val BLUR_DP = 10.dp
private const val SELECTED_ALPHA = 0.1f

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IngredientsPizzaAnimation(state: IngredientsPizzaTypeUiState) {
    // define the starting and ending positions of the animation
    val endPosition = Offset(0f, 0f)
    val startPosition = Offset(0f, -200f)

    // create Animatable states for rotation, scale, position, and blur
    val rotation by animateFloatAsState(targetValue = if (state.isSelected) ROTATION_ANGLE else 0f)
    val scale by animateFloatAsState(targetValue = if (state.isSelected) 1f else INITIAL_SCALE)
    val position by animateOffsetAsState(targetValue = if (state.isSelected) endPosition else startPosition)
    val blur by animateDpAsState(targetValue = if (state.isSelected) 0.dp else BLUR_DP)

    AnimatedVisibility(
        visible = state.isSelected,
        enter = scaleIn(initialScale = INITIAL_SCALE) + fadeIn(),
        exit = fadeOut()
    ) {
        Image(
            painter = painterResource(id = state.image),
            contentDescription = "ingredient ${state.id}",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = position.x,
                    translationY = position.y
                )
                .blur(blur)
        )
    }
}

@Composable
private fun PizzaIngredients(
    state: IngredientsPizzaTypeUiState,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .clickable { onClick() }
        .background(if (isSelected) Color.Green.copy(alpha = SELECTED_ALPHA) else Color.LightGray)
        .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = state.icon),
            contentDescription = "ingredient ${state.id}"
        )
    }
}