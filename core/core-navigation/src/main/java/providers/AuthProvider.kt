package providers

interface AuthProvider {
    fun isAuthenticated(): Boolean
}