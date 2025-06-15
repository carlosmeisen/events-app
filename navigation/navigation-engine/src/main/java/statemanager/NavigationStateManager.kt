package statemanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import backstack.BackStackTracker
import androidx.compose.runtime.getValue

@Composable
fun NavigationStateManager(
    navController: NavHostController,
    backStackTracker: BackStackTracker,
    content: @Composable () -> Unit
) {
    // Track navigation changes and update our back stack tracker
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Update tracker when navigation changes
    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            // Simple tracking - you might want more sophisticated logic
            val currentBackStack = backStackTracker.backStack

            // If this is a new route (not going back), push it
            if (currentBackStack.isEmpty() || currentBackStack.last() != route) {
                // Check if we're going back (route exists in stack)
                val existingIndex = currentBackStack.indexOf(route)
                if (existingIndex != -1) {
                    // We're going back - pop everything after this route
                    while (backStackTracker.backStack.size > existingIndex + 1) {
                        backStackTracker.pop()
                    }
                } else {
                    // New route - push it
                    backStackTracker.push(route)
                }
            }
        }
    }

    // Handle navigation lifecycle
    DisposableEffect(navController) {
        onDispose {
            // Clean up if needed
        }
    }

    content()
}