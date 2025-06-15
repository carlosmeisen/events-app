package analytics

sealed class NavigationEvent {
    data class RouteChanged(val from: String, val to: String) : NavigationEvent()
    data class DeepLinkHandled(val link: String) : NavigationEvent()
    data class ScreenViewed(val screenName: String) : NavigationEvent()
    data class BackNavigation (val currentRoute: String) : NavigationEvent()
}