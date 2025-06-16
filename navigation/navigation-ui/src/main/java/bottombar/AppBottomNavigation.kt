package bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.navigation_ui.R
import internal.AppDestination
import internal.NavigationCommand
import kotlinx.coroutines.channels.SendChannel

@Composable
fun AppBottomNavigation(
    navController: NavHostController,
    navigationChannel: SendChannel<NavigationCommand>,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // You could add UI-specific validation here
    val canNavigateToSettings = remember(currentRoute) {
        currentRoute in setOf(
            AppDestination.Home.route,
            AppDestination.Calendar.route,
            AppDestination.Favorites.route
        )
    }

    val canNavigateToFavorites = remember(currentRoute) {
        currentRoute in setOf(
            AppDestination.Home.route,
        )
    }

    val canNavigateToCalendar = remember(currentRoute) {
        currentRoute in setOf(
            AppDestination.Home.route,
        )
    }

    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = stringResource(id = R.string.navigation_home)
                )
            },
            label = { Text(text = stringResource(id = R.string.navigation_home)) },
            selected = currentRoute == AppDestination.Home.route,
            onClick = {
                navigationChannel.trySend(
                    NavigationCommand.To(
                        destination = AppDestination.Home,
                        popUpToRoute = AppDestination.Home.route,
                        singleTop = true
                    )
                )
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.navigation_settings)
                )
            },
            label = { Text(text = stringResource(id = R.string.navigation_settings)) },
            selected = currentRoute == AppDestination.Settings.route,
            enabled = canNavigateToSettings,
            onClick = {
                if (canNavigateToSettings) {
                    navigationChannel.trySend(
                        NavigationCommand.To(
                            destination = AppDestination.Settings,
                            popUpToRoute = AppDestination.Home.route,
                            singleTop = true
                        )
                    )
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Star,
                    contentDescription = stringResource(id = R.string.navigation_favorites)
                )
            },
            label = { Text(text = stringResource(id = R.string.navigation_favorites)) },
            selected = currentRoute == AppDestination.Favorites.route,
            enabled = canNavigateToFavorites,
            onClick = {
                if (canNavigateToFavorites) {
                    navigationChannel.trySend(
                        NavigationCommand.To(
                            destination = AppDestination.Favorites,
                            popUpToRoute = AppDestination.Home.route,
                            singleTop = true
                        )
                    )
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = stringResource(id = R.string.navigation_events)
                )
            },
            label = { Text(text = stringResource(id = R.string.navigation_events)) },
            selected = currentRoute == AppDestination.Calendar.route,
            enabled = canNavigateToFavorites,
            onClick = {
                if (canNavigateToCalendar) {
                    navigationChannel.trySend(
                        NavigationCommand.To(
                            destination = AppDestination.Calendar,
                            popUpToRoute = AppDestination.Home.route,
                            singleTop = true
                        )
                    )
                }
            }
        )
    }
}