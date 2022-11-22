package com.example.catalogueapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalPagerApi::class)
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
    var currentColor by remember { mutableStateOf(colour2) }

    LaunchedEffect(Unit) {
        buttonVisible = true
        AppNavigation.navBarVisible = false

        coroutineScope.launch {
            snapshotFlow { pagerState.currentPage }.collectLatest { page ->
                Timber.d("Current page: $page")
                currentColor = if (currentColor == colour1) colour2 else colour1
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
            Crossfade(targetState = currentColor, animationSpec = tween(500)) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(), color = it
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
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPagerIndicator(
                pagerState, activeColor = Color.White,
            )

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = buttonVisible,
                enter = fadeIn(
                    animationSpec = tween(2000)
                ),
            ) {
                PrimaryButton(
                    "Let me in",
                    onClick = {
                        onNextClick()
                    }
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
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