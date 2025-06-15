package providers

interface NetworkProvider {
    fun isAvailable(): Boolean
}