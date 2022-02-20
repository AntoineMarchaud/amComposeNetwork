package com.amarchaud.composenetwork.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.amarchaud.composenetwork.ui.graph.enterTransition
import com.amarchaud.composenetwork.ui.graph.exitTransitiob
import com.amarchaud.composenetwork.ui.graph.popEnterTransition
import com.amarchaud.composenetwork.ui.graph.popExitTransitiob
import com.amarchaud.composenetwork.ui.screen.login.LoginComposable
import com.amarchaud.composenetwork.ui.screen.login.LoginViewModel
import com.amarchaud.composenetwork.ui.screen.navigate.NavigateComposable
import com.amarchaud.composenetwork.ui.screen.navigate.NavigateViewModel
import com.amarchaud.composenetwork.ui.theme.ComposeNetworkTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    object RouteComposable {
        const val navigable = "navigable"
        const val login = "login"
    }


    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // draw behind nav / status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            ComposeNetworkTheme {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = RouteComposable.navigable
                ) {


                    composable(
                        route = RouteComposable.navigable,
                        enterTransition = { enterTransition },
                        exitTransition = { exitTransitiob },
                        popEnterTransition = { popEnterTransition },
                        popExitTransition = { popExitTransitiob }) {

                        val viewModel = hiltViewModel<NavigateViewModel>()
                        NavigateComposable(
                            viewModel = viewModel,
                            onConnectionClicked = {
                                navController.navigate(route = RouteComposable.login)
                            }
                        )
                    }

                    composable(
                        route = RouteComposable.login,
                        enterTransition = { enterTransition },
                        exitTransition = { exitTransitiob },
                        popEnterTransition = { popEnterTransition },
                        popExitTransition = { popExitTransitiob }) {

                        val viewModel = hiltViewModel<LoginViewModel>()
                        LoginComposable(
                            viewModel = viewModel,
                            isConnectedOk = {
                                navController.navigateUp()
                            })
                    }
                }
            }
        }
    }
}