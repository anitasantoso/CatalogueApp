package com.example.catalogueapp.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
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
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier
                            .weight(3.0f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = content[page],
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1.0f)
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(2.0f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(
                modifier = Modifier
                    .weight(3.0f)
            )
            Column(
                modifier = Modifier
                    .weight(1.0f),
                verticalArrangement = Arrangement.Center
            ) {
                HorizontalPagerIndicator(
                    pagerState, activeColor = Color.White,
                )
            }
            Column(
                modifier = Modifier
                    .weight(2.0f),
                verticalArrangement = Arrangement.Center
            ) {
                ScaleInAnimation(buttonVisible) {
                    PrimaryButton(
                        "Let me in",
                        onClick = onNextClick
                    )
                }
            }
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