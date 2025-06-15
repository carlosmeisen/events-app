class DefaultGenericLogger(
    private val logWriter: LogWriter
) : GenericLogger {
    override fun logDebug(message: String, tag: String) {
        logWriter.debug(tag, message)
    }

    override fun logInfo(message: String, tag: String) {
        logWriter.info(tag, message)
    }

    override fun logWarning(message: String, tag: String) {
        logWriter.warning(tag, message)
    }

    override fun logError(message: String, tag: String, throwable: Throwable?) {
        logWriter.error(tag, message, throwable)
    }
}