package analytics

data class NavigationStats(
    val totalNavigations: Int = 0,
    val validationFailures: Int = 0,
    val mostVisitedRoutes: Map<String, Int> = mapOf(),
    val averageValidationTime: Long = 0L
)