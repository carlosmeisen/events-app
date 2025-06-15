class AndroidLogWriter : LogWriter {
    override fun debug(tag: String, message: String) {
        android.util.Log.d(tag, message)
    }

    override fun info(tag: String, message: String) {
        android.util.Log.i(tag, message)
    }

    override fun warning(tag: String, message: String) {
        android.util.Log.w(tag, message)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        android.util.Log.e(tag, message, throwable)
    }
}