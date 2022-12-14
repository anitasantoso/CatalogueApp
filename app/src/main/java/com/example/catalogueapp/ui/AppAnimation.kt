package com.example.catalogueapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable

const val ANIM_DURATION = 300L

@Composable
fun FadeInAnimation(visible: Boolean, content: @Composable() AnimatedVisibilityScope.() -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(ANIM_DURATION.toInt())
        ),
        exit = fadeOut(
            animationSpec = tween(ANIM_DURATION.toInt())
        ), content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScaleInAnimation(visible: Boolean, delay: Int = 0, content: @Composable() AnimatedVisibilityScope.() -> Unit) {
    AnimatedVisibility(
        visible,
        enter = scaleIn(
            animationSpec = tween(ANIM_DURATION.toInt(), delay)
        ),
        exit = scaleOut(
            animationSpec = tween(ANIM_DURATION.toInt(), delay)
        ), content = content
    )
}