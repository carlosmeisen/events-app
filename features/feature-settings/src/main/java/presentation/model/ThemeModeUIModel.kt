package presentation.model

enum class ThemeModeUIModel {
    LIGHT,
    DARK,
    SYSTEM_DEFAULT;

    fun toDisplayName(): String {
        return when(this) {
            LIGHT -> "Light"
            DARK -> "Dark"
            SYSTEM_DEFAULT -> "System Default"
        }
    }
}