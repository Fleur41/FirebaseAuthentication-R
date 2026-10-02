package com.sam.firebaseauthentication_r.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sam.firebaseauthentication_r.authentication.signin.SignInScreen
import com.sam.firebaseauthentication_r.authentication.signup.SignUpScreen
import com.sam.firebaseauthentication_r.components.slideIntoContainerAnimation
import com.sam.firebaseauthentication_r.components.slideOutOfContainerAnimation
import com.sam.firebaseauthentication_r.detail.DetailScreen
import com.sam.firebaseauthentication_r.authentication.forgotpassword.ForgotPasswordScreen
import com.sam.firebaseauthentication_r.home.HomeScreen
import com.sam.firebaseauthentication_r.home.HomeViewModel
import com.sam.firebaseauthentication_r.splash.SplashScreen

@Composable
fun AppNavigation(
    startDestination: NavigationDestination,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ){
        composable (
            route = NavigationDestination.SignIn.route,
            enterTransition = { slideIntoContainerAnimation() },
            exitTransition = { slideOutOfContainerAnimation() }
        ){ backStackEntry ->
            SignInScreen(
                authViewModel = hiltViewModel(backStackEntry),
                onSignUpClick = {
                    navController.navigate(NavigationDestination.SignUp.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(NavigationDestination.ForgotPassword.route)
                }
            )
        }

        composable (
            route = NavigationDestination.SignUp.route,
            enterTransition = { slideIntoContainerAnimation(towards = SlideDirection.Right) },
            exitTransition = { slideOutOfContainerAnimation(towards = SlideDirection.Left) }
        ){backStackEntry ->
            val parentEntry = remember(backStackEntry) {navController.getBackStackEntry(NavigationDestination.SignIn.route)}
            SignUpScreen(
                authViewModel = hiltViewModel(parentEntry),
                onBack = {navController.popBackStack()},
            )
        }

        composable (
            route = NavigationDestination.Home.route,
            enterTransition = { slideIntoContainerAnimation(towards = SlideDirection.Right) },
            exitTransition = { slideOutOfContainerAnimation(towards = SlideDirection.Left) }
        ){
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                homeViewModel = homeViewModel,
                onDetailClick = { navController.navigate(NavigationDestination.Detail.route)}
            )
        }

        composable (
            route = NavigationDestination.Detail.route,
            enterTransition = { slideIntoContainerAnimation(towards = SlideDirection.Right) },
            exitTransition = { slideOutOfContainerAnimation(towards = SlideDirection.Left) }
        ){backStackEntry ->
            val parentEntry = remember(backStackEntry) {navController.getBackStackEntry(NavigationDestination.Home.route)}
            DetailScreen(
                homeViewModel = hiltViewModel(parentEntry),
                onBackClick = {navController.popBackStack()}
            )
        }

        composable (
            route = NavigationDestination.Splash.route,
        ){
            SplashScreen()
        }

        // Forgot Password Destination
        composable(
            route = NavigationDestination.ForgotPassword.route,
            enterTransition = { slideIntoContainerAnimation(towards = SlideDirection.Right) },
            exitTransition = { slideOutOfContainerAnimation(towards = SlideDirection.Left) }
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavigationDestination.SignIn.route)
            }
            ForgotPasswordScreen(
                authViewModel = hiltViewModel(parentEntry),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}