interface GenericLogger {
    fun logDebug(message: String, tag: String = "App")
    fun logInfo(message: String, tag: String = "App")
    fun logWarning(message: String, tag: String = "App")
    fun logError(message: String, tag: String = "App", throwable: Throwable? = null)
}