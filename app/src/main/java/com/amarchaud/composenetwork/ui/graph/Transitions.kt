package com.amarchaud.composenetwork.ui.graph

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

@OptIn(ExperimentalAnimationApi::class)
internal val enterTransition =
    fadeIn(tween(300)).plus(scaleIn(animationSpec = tween(300), initialScale = 0.75f))

internal val exitTransitiob =
    slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it })

internal val popEnterTransition =
    slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -it })

@OptIn(ExperimentalAnimationApi::class)
internal val popExitTransitiob =
    fadeOut(tween(300)).plus(scaleOut(animationSpec = tween(300), targetScale = 0.75f))