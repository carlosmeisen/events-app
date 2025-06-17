package appinitialization

sealed class InitializationAction {
    data class LoadUserPreferences(val languageCode: String) : InitializationAction()
    class APICalls : InitializationAction()
}