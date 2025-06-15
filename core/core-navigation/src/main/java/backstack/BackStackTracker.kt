package backstack

class BackStackTracker {
    private val _backStack = mutableListOf<String>()
    val backStack: List<String> get() = _backStack.toList()

    fun push(route: String) {
        _backStack.add(route)
    }

    fun pop(): String? {
        return if (_backStack.isNotEmpty()) {
            _backStack.removeLastOrNull()
        } else null
    }

    fun popUpTo(route: String, inclusive: Boolean = false): List<String> {
        val poppedRoutes = mutableListOf<String>()

        while (_backStack.isNotEmpty()) {
            val currentRoute = _backStack.last()
            if (currentRoute == route) {
                if (inclusive) {
                    poppedRoutes.add(_backStack.removeLastOrNull() ?: break)
                }
                break
            } else {
                poppedRoutes.add(_backStack.removeLastOrNull() ?: break)
            }
        }

        return poppedRoutes
    }

    fun contains(route: String): Boolean = _backStack.contains(route)

    fun clear() {
        _backStack.clear()
    }

    fun isEmpty(): Boolean = _backStack.isEmpty()

    fun size(): Int = _backStack.size
}