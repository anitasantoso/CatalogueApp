package com.example.catalogueapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable

const val FADE_ANIM_DURATION = 500L

@Composable
fun FadeInAnimation(visible: Boolean, content: @Composable() AnimatedVisibilityScope.() -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(2000)
        ),
        exit = fadeOut(
            animationSpec = tween(2000)
        ), content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScaleInAnimation(visible: Boolean, delay: Int = 0, content: @Composable() AnimatedVisibilityScope.() -> Unit) {
    AnimatedVisibility(
        visible,
        enter = scaleIn(
            animationSpec = tween(FADE_ANIM_DURATION.toInt(), delay)
        ),
        exit = scaleOut(
            animationSpec = tween(FADE_ANIM_DURATION.toInt(), delay)
        ), content = content
    )
}