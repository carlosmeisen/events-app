package mapper.theme

import preference.ThemeMode
import presentation.ui.UiThemeMode

fun ThemeMode.toUiThemeMode(): UiThemeMode {
    return when (this) {
        ThemeMode.LIGHT -> UiThemeMode.LIGHT
        ThemeMode.DARK -> UiThemeMode.DARK
        ThemeMode.SYSTEM_DEFAULT -> UiThemeMode.SYSTEM_DEFAULT
    }
}

fun UiThemeMode.toDomainThemeMode(): ThemeMode {
    return when (this) {
        UiThemeMode.LIGHT -> ThemeMode.LIGHT
        UiThemeMode.DARK -> ThemeMode.DARK
        UiThemeMode.SYSTEM_DEFAULT -> ThemeMode.SYSTEM_DEFAULT
    }
}