class NoOpAnalyticsTracker : AnalyticsTracker {
    override fun trackEvent(name: String, properties: Map<String, Any>) {
        // Do nothing in debug/testing
    }

    override fun trackScreen(screenName: String, properties: Map<String, Any>) {
        // Do nothing in debug/testing
    }
}