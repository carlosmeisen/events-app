package com.example.festiveapp.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bottombar.AppBottomNavigation
import internal.AppDestination
import internal.NavigationCommand
import kotlinx.coroutines.channels.SendChannel
import orchestrator.NavigationOrchestrator
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import preference.AppTheme
import presentation.ui.CalendarScreen
import presentation.ui.FavoritesScreen
import presentation.ui.HomeScreen
import presentation.ui.SettingsScreen
import presentation.viewmodel.AppThemeViewModel
import validators.ComposeNavigationValidator

class MainActivity : ComponentActivity(), AndroidScopeComponent {

    // Koin scope for the activity, ensuring dependencies are retained across config changes
    override val scope: Scope by activityRetainedScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appThemeViewModel: AppThemeViewModel by inject()
            val themeState by appThemeViewModel.themeState.collectAsState()
            val systemInDarkTheme = isSystemInDarkTheme()

            val shouldUseDarkTheme = if (themeState.isSystemDefault) {
                systemInDarkTheme
            } else {
                themeState.shouldUseDarkTheme
            }

            AppTheme(darkTheme = shouldUseDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navigationCommandChannel: SendChannel<NavigationCommand> = remember {
                        getKoin().get<SendChannel<NavigationCommand>>()
                    }
                    val navigationOrchestrator: NavigationOrchestrator =
                        remember(navController, getKoin()) {
                        getKoin().get { parametersOf(navController) }
                    }

                    LaunchedEffect(key1 = navigationOrchestrator) {
                        navigationOrchestrator.startListening(lifecycleScope = this@MainActivity.lifecycleScope)
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            AppBottomNavigation(
                                navController = navController,
                                navigationChannel = navigationCommandChannel
                        )
                        }
                    ) { innerPadding ->
                        ComposeNavigationValidator(navController = navController) {
                            NavHost(
                                navController = navController,
                                startDestination = AppDestination.Home.route,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable(route = AppDestination.Home.route) { HomeScreen() }
                                composable(route = AppDestination.Calendar.route) { CalendarScreen() }
                                composable(route = AppDestination.Settings.route) { SettingsScreen() }
                                composable(route = AppDestination.Favorites.route) { FavoritesScreen() }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        // For previews, you might need a mock NavController
        // or create individual previews for each screen, passing dummy data.
    }
}