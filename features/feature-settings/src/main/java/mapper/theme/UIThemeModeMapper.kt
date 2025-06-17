package mapper.theme

import preference.ThemeMode
import presentation.model.ThemeModeUIModel

fun ThemeMode.toUiThemeMode(): ThemeModeUIModel {
    return when (this) {
        ThemeMode.LIGHT -> ThemeModeUIModel.LIGHT
        ThemeMode.DARK -> ThemeModeUIModel.DARK
        ThemeMode.SYSTEM_DEFAULT -> ThemeModeUIModel.SYSTEM_DEFAULT
    }
}

fun ThemeModeUIModel.toDomainThemeMode(): ThemeMode {
    return when (this) {
        ThemeModeUIModel.LIGHT -> ThemeMode.LIGHT
        ThemeModeUIModel.DARK -> ThemeMode.DARK
        ThemeModeUIModel.SYSTEM_DEFAULT -> ThemeMode.SYSTEM_DEFAULT
    }
}