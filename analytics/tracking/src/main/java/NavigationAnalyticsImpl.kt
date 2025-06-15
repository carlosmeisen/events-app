import analytics.NavigationEvent
import analytics.NavigationStats

class NavigationAnalyticsImpl(
    private val tracker: AnalyticsTracker
) : NavigationAnalytics {
    override fun trackEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.RouteChanged -> {
                tracker.trackEvent(
                    "navigation_route_changed", mapOf(
                        "from_route" to event.from,
                        "to_route" to event.to
                    )
                )
            }

            is NavigationEvent.DeepLinkHandled -> {
                tracker.trackEvent(
                    "deeplink_handled", mapOf(
                        "deeplink_url" to event.link
                    )
                )
            }

            is NavigationEvent.BackNavigation -> {
                tracker.trackEvent(
                    "back_navigation", mapOf(
                        "current_route" to event.currentRoute
                    )
                )
            }
            is NavigationEvent.ScreenViewed -> {
                tracker.trackScreen(
                    "screen_viewed", mapOf(
                        "screen_name" to event.screenName
                    )
                )
            }
        }
    }

    override fun getStats(): NavigationStats {
        TODO("Not yet implemented")
    }
}