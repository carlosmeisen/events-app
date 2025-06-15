package destinations

import internal.AppDestination

object DestinationRegistry {
    private val validDestinations = setOf(
        AppDestination.Home.route,
        AppDestination.Settings.route,
        AppDestination.Calendar.route,
        AppDestination.Favorites.route
    )

    private val routePatterns = listOf(
        Regex("^event_detail_route/[^/]+$"),
        Regex("^search_results_route(\\?query=.+)?$"),
        Regex("^event_list_route(\\?category=.+)?$")
    )

    fun isValidDestination(route: String): Boolean {
        return validDestinations.contains(route) ||
                routePatterns.any { it.matches(route) }
    }

    fun getValidDestinations(): Set<String> = validDestinations
}