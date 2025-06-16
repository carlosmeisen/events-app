package com.example.festiveapp.presentation.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import java.util.Locale
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
import com.example.feature_login.presentation.ui.LoginScreen
import internal.AppDestination
import internal.NavigationCommand
import kotlinx.coroutines.channels.SendChannel
import orchestrator.NavigationOrchestrator
import org.koin.android.ext.android.getKoin
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import preference.AppTheme
import presentation.ui.CalendarScreen
import presentation.ui.FavoritesScreen
import presentation.ui.HomeScreen
// Import LanguageSelectionScreen - assuming it's in the same package as SettingsScreen
import presentation.ui.LanguageSelectionScreen
import presentation.ui.SettingsScreen
import presentation.viewmodel.AppThemeViewModel
import presentation.viewmodel.LanguageViewModel
import presentation.viewmodel.SettingsViewModel
import validators.ComposeNavigationValidator

class MainActivity : ComponentActivity(), AndroidScopeComponent {

    // Koin scope for the activity, ensuring dependencies are retained across config changes
    override val scope: Scope by activityRetainedScope()

    // Function to update the app's locale
    private fun updateLocale(languageCode: String, context: Context) {
        val parts = languageCode.split("-")
        val language = parts[0]
        val country = parts.getOrElse(1) { "" }.uppercase(Locale.ROOT)
        val locale = Locale(language, country)

        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        // For older APIs, activity recreation might be needed via finish() + startActivity(intent)
        // or just recreate() for API 11+ if issues persist.
        // For now, relying on Compose recomposition and resource reloading.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initial locale setup could be considered here if a synchronous way to get
        // the preference on app start was available. DataStore is async, so dynamic
        // update via LaunchedEffect is the primary mechanism for this subtask.

        setContent {
            val appThemeViewModel: AppThemeViewModel by inject()
            val themeState by appThemeViewModel.themeState.collectAsState()
            val systemInDarkTheme = isSystemInDarkTheme()

            val languageViewModel: LanguageViewModel = koinViewModel() // Get LanguageViewModel
            val languageState by languageViewModel.languageState.collectAsState()

            // Effect to update locale when language preference changes
            LaunchedEffect(languageState.currentLanguageCode) {
                if (languageState.currentLanguageCode.isNotEmpty()) {
                    val currentSystemLocale = resources.configuration.locales[0]
                    val selectedLocale = Locale(
                        languageState.currentLanguageCode.split("-")[0],
                        languageState.currentLanguageCode.split("-").getOrElse(1) { "" }.uppercase(Locale.ROOT)
                    )

                    if (currentSystemLocale.language != selectedLocale.language ||
                        currentSystemLocale.country != selectedLocale.country) {
                        updateLocale(languageState.currentLanguageCode, this@MainActivity)
                        // Optional: recreate() // To force resource reloading if necessary
                    }
                }
            }

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

                    // This LaunchedEffect for navigationOrchestrator should be distinct from the language one.
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
                                composable(route = AppDestination.Settings.route) {
                                    val settingsViewModel: SettingsViewModel = koinViewModel()

                                    // LaunchedEffect to handle navigation signals from SettingsViewModel
                                    LaunchedEffect(key1 = settingsViewModel) { // Key on viewModel instance for safety
                                        settingsViewModel.navigateToLanguageSelection.collectLatest {
                                            navController.navigate(AppDestination.LanguageSelection.route)
                                        }
                                    }

                                    SettingsScreen(
                                        settingsViewModel = settingsViewModel,
                                        // appThemeViewModel and languageViewModel are koinViewModel by default in SettingsScreen
                                        onNavigateToLanguageSelection = {
                                            // This lambda, when invoked by SettingsScreen UI,
                                            // will call the ViewModel method to signal navigation.
                                            settingsViewModel.onLanguageSettingsClicked()
                                        }
                                    )
                                }
                                composable(route = AppDestination.Favorites.route) { FavoritesScreen() }
                                composable(route = AppDestination.Login.route) { LoginScreen() }
                                composable(route = AppDestination.LanguageSelection.route) { // New Screen
                                    LanguageSelectionScreen(
                                        onNavigateUp = { navController.navigateUp() }
                                        // LanguageViewModel is koinViewModel by default in LanguageSelectionScreen
                                    )
                                }
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