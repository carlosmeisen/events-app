package ui.uimodel.externalApps

sealed class UiExternalAppData {
    data class Email(val email: String) : UiExternalAppData()
}