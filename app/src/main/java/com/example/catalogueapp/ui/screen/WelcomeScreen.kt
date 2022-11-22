package com.example.catalogueapp.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.catalogueapp.AppNavigation
import com.example.catalogueapp.ui.ScaleInAnimation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun WelcomeScreen(
    onNextClick: () -> Unit
) {
    val content = listOf(
        "Hello!\nSwipe left to see more text.",
        "This is some more text.",
        "And more text here."
    )
    var buttonVisible by remember { mutableStateOf(false) }

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    var colour1 = MaterialTheme.colorScheme.onPrimaryContainer
    var colour2 = Color.Gray
    var state by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        if (state) colour1 else colour2,
        animationSpec = tween(1000)
    )

    LaunchedEffect(Unit) {
        buttonVisible = true
        AppNavigation.navBarVisible = false

        coroutineScope.launch {
            snapshotFlow { pagerState.currentPage }.collectLatest { page ->
                Timber.d("Current page: $page")
                state = !state
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            content.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
        ) { page ->
            Surface(
                modifier = Modifier
                    .fillMaxSize(), color = backgroundColor
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = content[page],
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPagerIndicator(
                pagerState, activeColor = Color.White,
            )

            Spacer(modifier = Modifier.height(40.dp))
            SlideInButton(buttonVisible, onNextClick)
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * Taken from https://developer.android.com/reference/kotlin/androidx/compose/animation/package-summary#slideInVertically(androidx.compose.animation.core.FiniteAnimationSpec,kotlin.Function1)
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideInButton(buttonVisible: Boolean, onNextClick: () -> Unit) {
    ScaleInAnimation(buttonVisible) {
        PrimaryButton(
            "Let me in",
            onClick = {
                onNextClick()
            }
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    height: Dp = 58.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .defaultMinSize(200.dp)
            .height(height)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.Center)
        )
    }
}