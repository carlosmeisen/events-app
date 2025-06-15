package internal

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Defines the sealed class for all possible destinations within the application's
 * Compose navigation graph. Each destination holds its route and a list of arguments.
 */
sealed class AppDestination(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    // Top-level destinations without arguments
    data object Home : AppDestination(route = "home_route")
    data object Settings : AppDestination(route = "settings_route")
    data object Calendar : AppDestination(route = "calendar_route")
    data object Favorites : AppDestination(route = "favorites_route")

    // Example of a destination with a required argument
    data class EventDetail(val eventId: String = "{eventId}") :
        AppDestination(
            route = "event_detail_route/{eventId}",
            navArguments = listOf(
                navArgument(name = "eventId") { type = NavType.StringType }
            )
        ) {
        /**
         * Creates the concrete route string for navigating to EventDetail with a specific ID.
         */
        fun createRoute(eventId: String): String = "event_detail_route/$eventId"
    }

    // Example of a destination with an optional argument
    data class SearchResults(val query: String? = "{query}") :
        AppDestination(
            route = "search_results_route?query={query}",
            navArguments = listOf(
                navArgument(name = "query") {
                    type = NavType.StringType
                    nullable = true // Mark as nullable for optional arguments
                    defaultValue = null
                }
            )
        ) {
        /**
         * Creates the concrete route string for navigating to SearchResults with an optional query.
         */
        fun createRoute(query: String? = null): String =
            query?.let {
                "search_results_route?query=$it"
            } ?: "search_results_route"
    }

    /**
     * Represents a list of events, optionally filtered by category.
     * @param category The optional category to filter events by.
     */
    data class EventList(val category: String? = "{category}") :
        AppDestination(
            route = "event_list_route?category={category}",
            navArguments = listOf(
                navArgument(name = "category") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
        /**
         * Creates the concrete route string for navigating to the EventList,
         * with an optional category.
         */
        fun createRoute(category: String? = null): String =
            category?.let {
                "event_list_route?category=$it"
            } ?: "event_list_route"
    }

    // Add other internal app destinations as needed
    // data object Settings : AppDestination("settings_route")
}