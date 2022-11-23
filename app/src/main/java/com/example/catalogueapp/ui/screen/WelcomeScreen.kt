package com.example.catalogueapp.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.catalogueapp.ui.ScaleInAnimation
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
    var state by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        if (state) colour1 else colour2,
        animationSpec = tween(1000)
    )

    LaunchedEffect(Unit) {
        buttonVisible = true

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

        // box content
        HorizontalPager(
            content.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
        ) { page ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                // take up upper half of the screen, align top
                Column(
                    modifier = Modifier
                        .height(this.maxHeight / 2)
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LargeText(content[page])
                }
            }
        }

        // box content
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

            // occupy 1/3 of bottom screen
            Column(
                modifier = Modifier
                    .height(this.maxHeight / 2)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                HorizontalPagerIndicator(
                    pagerState, activeColor = Color.White,
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    ScaleInAnimation(buttonVisible) {
                        LargeButton(
                            "Let me in",
                            onClick = onNextClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LargeText(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.displayLarge,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LargeButton(
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

@Composable
@Preview(name = "welcome_phone", showSystemUi = true, showBackground = true, device = Devices.PHONE)
fun WelcomePhonePreview() {
    WelcomeScreen {}
}

@Composable
@Preview(
    name = "welcome_tablet",
    showSystemUi = true,
    showBackground = true,
    device = Devices.TABLET
)
fun WelcomeTabletPreview() {
    WelcomeScreen {}
}