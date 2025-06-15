import analytics.NavigationError
import analytics.NavigationEvent

interface NavigationLogger {
    fun logNavigation(event: NavigationEvent)
    fun logError(error: NavigationError)
}