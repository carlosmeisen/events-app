interface AnalyticsTracker {
    fun trackEvent(name: String, properties: Map<String, Any> = emptyMap())
    fun trackScreen(screenName: String, properties: Map<String, Any> = emptyMap())
}