package navhost

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import internal.AppDestination

/**
 * Defines the main navigation host for the application's Compose UI.
 * This is where all [AppDestination] routes are mapped to their respective Composables.
 *
 * @param navController The [NavController] instance used to manage navigation.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homeScreen: @Composable () -> Unit,
    calendarScreen: @Composable () -> Unit,
    settingsScreen: @Composable () -> Unit,
    favoritesScreen: @Composable () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route,
        modifier = modifier
    ) {
        composable(route = AppDestination.Home.route) {
            homeScreen()
        }
        composable(route = AppDestination.Settings.route) {
            settingsScreen()
        }
        composable(route = AppDestination.Calendar.route) {
            calendarScreen()
        }
        composable(route = AppDestination.Favorites.route) {
            favoritesScreen()
        }
    }
}