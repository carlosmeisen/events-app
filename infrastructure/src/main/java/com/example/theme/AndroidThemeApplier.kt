package com.example.theme

import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import preference.ThemeMode
import theme.GetThemePreferenceUseCase
import ui.theme.ThemeApplier

/**
 * Android-specific implementation of [ThemeApplier].
 * This class observes the theme changes from the domain layer (via [GetThemePreferenceUseCase])
 * and applies them using [AppCompatDelegate].
 *
 * @param getThemePreferenceUseCase The use case that provides the stream of theme mode changes.
 */
class AndroidThemeApplier(
    private val getThemePreferenceUseCase: GetThemePreferenceUseCase
) : ThemeApplier {

    /**
     * Starts observing theme preferences and setting [AppCompatDelegate.setDefaultNightMode].
     * This method launches a coroutine in the given scope to continuously
     * listen for theme changes and update the UI accordingly.
     *
     * @param scope The [CoroutineScope] where the theme application coroutine will be launched.
     */
    override fun startApplying(scope: CoroutineScope) {
        scope.launch {
            getThemePreferenceUseCase().collect { themeMode ->
                // Map the domain theme mode to an Android-specific AppCompatDelegate mode
                val nightMode = when (themeMode) {
                    ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    ThemeMode.SYSTEM_DEFAULT -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }
        }
    }
}