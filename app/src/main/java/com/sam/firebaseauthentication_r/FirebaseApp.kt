package com.sam.firebaseauthentication_r

import androidx.compose.runtime.Composable
import com.sam.firebaseauthentication_r.navigation.AppNavigation
import com.sam.firebaseauthentication_r.navigation.NavigationDestination


@Composable
fun FirebaseApp(
    startDestination: NavigationDestination
) {
    AppNavigation(startDestination = startDestination)
}