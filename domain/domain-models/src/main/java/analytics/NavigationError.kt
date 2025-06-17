package analytics

sealed class NavigationError(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    data class ValidationFailed(
        val route: String,
        val validationErrors: List<String>,
        override val cause: Throwable? = null
    ) : NavigationError(
        message = "Navigation validation failed for route '$route': ${validationErrors.joinToString(", ")}",
        cause = cause
    )

    data class PermissionDenied(
        val route: String,
        val requiredPermissions: List<String>,
        override val cause: Throwable? = null
    ) : NavigationError(
        message = "Permission denied for route '$route'. Required permissions: ${requiredPermissions.joinToString(", ")}",
        cause = cause
    )

    data class RouteNotFound(
        val route: String,
        override val cause: Throwable? = null
    ) : NavigationError(
        message = "Route not found: '$route'",
        cause = cause
    )

    data class ConfigurationError(
        val route: String,
        val configurationIssue: String,
        override val cause: Throwable? = null
    ) : NavigationError(
        message = "Configuration error for route '$route': $configurationIssue",
        cause = cause
    )

    data class DeepLinkError(
        val deepLink: String,
        val reason: String,
        override val cause: Throwable? = null
    ) : NavigationError(
        message = "Deep link error for '$deepLink': $reason",
        cause = cause
    )

    data class UnknownError(
        val route: String? = null,
        override val cause: Throwable
    ) : NavigationError(
        message = "Unknown navigation error${route?.let { " for route '$it'" } ?: ""}: ${cause.message}",
        cause = cause
    )
}