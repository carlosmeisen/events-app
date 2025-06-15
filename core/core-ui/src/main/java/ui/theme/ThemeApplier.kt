package ui.theme

import kotlinx.coroutines.CoroutineScope

/**
 * Interface for applying theme changes to the application's UI.
 * This abstraction allows platform-specific implementations (e.g., Android's AppCompatDelegate)
 * to be decoupled from domain logic.
 */
interface ThemeApplier {
    /**
     * Starts the process of applying theme changes.
     * This method is typically called once during application startup,
     * and it should launch its observation logic within the provided [CoroutineScope].
     *
     * @param scope The [CoroutineScope] in which the theme application logic will run.
     */
    fun startApplying(scope: CoroutineScope)
}
