import analytics.NavigationError
import analytics.NavigationEvent
import analytics.NavigationEvent.BackNavigation
import analytics.NavigationEvent.DeepLinkHandled
import analytics.NavigationEvent.RouteChanged
import analytics.NavigationEvent.ScreenViewed

class DefaultNavigationLogger(
    private val logWriter: LogWriter
) : NavigationLogger {
    override fun logNavigation(event: NavigationEvent) {
        logWriter.debug("Navigation", "Event: ${event.javaClass.simpleName}")
        when (event) {
            is RouteChanged -> {
                logWriter.debug("Navigation", "Route changed: ${event.from} -> ${event.to}")
            }
            is ScreenViewed -> {
                logWriter.debug("Navigation", "Screen viewed: ${event.screenName}")
            }
            is BackNavigation -> {
                logWriter.debug("Navigation", "Back navigation from: ${event.currentRoute}")
            }
            is DeepLinkHandled -> {
                logWriter.debug("Navigation", "Deep link handled: ${event.link}")
            }
        }
    }

    override fun logError(error: NavigationError) {
        when (error) {
            is NavigationError.ValidationFailed -> {
                logWriter.error("Navigation", "Validation Failed - Route: ${error.route}", error.cause)
                logWriter.error("Navigation", "Validation Errors: ${error.validationErrors.joinToString(", ")}")
            }
            is NavigationError.PermissionDenied -> {
                logWriter.error("Navigation", "Permission Denied - Route: ${error.route}", error.cause)
                logWriter.error("Navigation", "Required Permissions: ${error.requiredPermissions.joinToString(", ")}")
            }
            is NavigationError.RouteNotFound -> {
                logWriter.error("Navigation", "Route Not Found: ${error.route}", error.cause)
            }
            is NavigationError.ConfigurationError -> {
                logWriter.error("Navigation", "Configuration Error - Route: ${error.route}", error.cause)
                logWriter.error("Navigation", "Issue: ${error.configurationIssue}")
            }
            is NavigationError.DeepLinkError -> {
                logWriter.error("Navigation", "Deep Link Error: ${error.deepLink}", error.cause)
                logWriter.error("Navigation", "Reason: ${error.reason}")
            }
            is NavigationError.UnknownError -> {
                logWriter.error("Navigation", "Unknown Error${error.route?.let { " - Route: $it" } ?: ""}", error.cause)
            }
        }
    }
}