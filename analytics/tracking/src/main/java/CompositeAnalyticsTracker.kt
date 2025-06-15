/**
 * Use this when having multiple AnalyticsTracker implementations
 */
class CompositeAnalyticsTracker(
    private val trackers: List<AnalyticsTracker>
) : AnalyticsTracker {
    override fun trackEvent(name: String, properties: Map<String, Any>) {
        trackers.forEach { it.trackEvent(name = name, properties = properties) }
    }

    override fun trackScreen(
        screenName: String,
        properties: Map<String, Any>
    ) {
        trackers.forEach { it.trackScreen(screenName = screenName, properties = properties) }
    }
}