package com.naufal.gameku.ui.navigation

import GameDetailScreen
import HomeScreen
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.get
import com.naufal.gameku.ui.features.favorite.FavoriteGameScreen

@Composable
fun GamekuGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                openGameDetailScreen = {
                    navController.navigate(Screen.GameDetail.createRoute(it))
                },
                openFavoriteScreen = {
                    navController.navigate(Screen.Favorite.route)
                }
            )
        }

        composable(Screen.GameDetail.route) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")
            requireNotNull(gameId) { "gameId parameter wasn't found. Please make sure it's set!" }
            GameDetailScreen(
                gameId = gameId.toInt(),
                openHomeScreen = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.Favorite.route) {
            FavoriteGameScreen(
                openGameDetailScreen = {
                    navController.navigate(Screen.GameDetail.createRoute(it))
                },
                openHomeScreen = {
                    navController.navigateUp()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object GameDetail : Screen("game_detail/{gameId}") {
        fun createRoute(gameId: Int) = "game_detail/$gameId"
    }

    object Favorite : Screen("favorite")
}

fun NavGraphBuilder.composable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    addDestination(
        ComposeNavigator.Destination(provider[ComposeNavigator::class], content).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}