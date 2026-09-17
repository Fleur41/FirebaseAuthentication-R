package com.sam.firebaseauthentication_r.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideIntoContainerAnimation(
    towards:SlideDirection = SlideDirection.End
) = slideIntoContainer(
    animationSpec = tween(
        durationMillis = 200,
        easing = EaseIn
    ),
    towards = towards
)

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutOfContainerAnimation(
    towards:SlideDirection = SlideDirection.End
) = slideOutOfContainer(
    animationSpec = tween(
        durationMillis = 200,
        easing = EaseOut
    ),
    towards = towards
)