package internal

import android.os.Bundle
import external.ExternalAppData

/**
 * Defines the sealed class for navigation commands that can be executed by the AppNavigator.
 * This includes internal app navigation and commands to launch external apps.
 */
sealed class NavigationCommand {
    /**
     * Navigates to a specific internal app destination.
     * @param destination The [AppDestination] to navigate to.
     * @param args Optional [Bundle] for arguments (less common with Compose Navigation's route arguments,
     * but useful for complex Parcelable/Serializable objects if a custom NavType isn't used).
     * @param inclusive Whether the `popUpToRoute` should also be popped from the back stack.
     * @param popUpToRoute The route to pop up to before navigating.
     * @param singleTop Whether to launch the destination as a single top (prevents multiple copies on top of stack).
     */
    data class To(
        val destination: AppDestination,
        val args: Bundle? = null, // Typically arguments are embedded in the route string
        val inclusive: Boolean = false,
        val popUpToRoute: String? = null,
        val singleTop: Boolean = false
    ) : NavigationCommand()

    /**
     * Navigates to a specific internal app destination using a direct route string.
     * This is useful when the route contains arguments directly.
     * @param route The full route string (e.g., "event_detail_route/123").
     * @param inclusive Whether the `popUpToRoute` should also be popped from the back stack.
     * @param popUpToRoute The route to pop up to before navigating.
     * @param singleTop Whether to launch the destination as a single top.
     */
    data class ToRoute(
        val route: String,
        val inclusive: Boolean = false,
        val popUpToRoute: String? = null,
        val singleTop: Boolean = false
    ) : NavigationCommand()

    /**
     * Navigates up in the navigation hierarchy (typically equivalent to the back button).
     */
    data object Up : NavigationCommand()

    /**
     * Pops the current destination off the back stack.
     */
    data object PopBackStack : NavigationCommand()

    /**
     * Pops the back stack up to a specific route.
     * @param route The route to pop up to.
     * @param inclusive Whether the specified `route` should also be popped from the back stack.
     */
    data class PopUpTo(
        val route: String,
        val inclusive: Boolean = false
    ) : NavigationCommand()

    /**
     * Commands to open an external application using the provided data.
     * @param data The [ExternalAppData] defining what external app to launch and with what data.
     */
    data class OpenExternalApp(val data: ExternalAppData) : NavigationCommand()
}