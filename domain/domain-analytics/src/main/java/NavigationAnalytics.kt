import analytics.NavigationEvent
import analytics.NavigationStats

interface NavigationAnalytics {
    fun trackEvent(event: NavigationEvent)
    fun getStats(): NavigationStats
}