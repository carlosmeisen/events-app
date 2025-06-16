package presentation.viewmodel

import GenericLogger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mapper.theme.toDomainThemeMode
import mapper.theme.toUiThemeMode
import presentation.model.ThemeModeUIModel
import theme.GetThemePreferenceUseCase
import theme.SaveThemePreferenceUseCase

data class AppThemeState(
    val themeMode: ThemeModeUIModel = ThemeModeUIModel.SYSTEM_DEFAULT,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isSystemDefault: Boolean
        get() = themeMode == ThemeModeUIModel.SYSTEM_DEFAULT

    val shouldUseDarkTheme: Boolean
        get() = themeMode == ThemeModeUIModel.DARK
}

class AppThemeViewModel(
    private val getThemePreferenceUseCase: GetThemePreferenceUseCase,
    private val saveThemePreferenceUseCase: SaveThemePreferenceUseCase,
    private val logger: GenericLogger
) : ViewModel() {

    private val _themeState = MutableStateFlow(AppThemeState())
    val themeState: StateFlow<AppThemeState> = _themeState.asStateFlow()

    init {
        observeThemePreference()
    }

    private fun observeThemePreference() {
        viewModelScope.launch {
            getThemePreferenceUseCase().collectLatest { themeMode ->
                _themeState.update { currentState ->
                    currentState.copy(themeMode = themeMode.toUiThemeMode())
                }
            }
        }
    }

    fun updateThemePreference(themeMode: ThemeModeUIModel) {
        viewModelScope.launch {
            _themeState.update { it.copy(isLoading = true, error = null) }

            val result = saveThemePreferenceUseCase(themeMode.toDomainThemeMode())

            result.fold(
                onSuccess = {
                    // Theme update will be handled by observeThemePreference()
                    _themeState.update { it.copy(isLoading = false, error = null) }
                },
                onFailure = { exception ->
                    logger.logError("AppThemeViewModel", "Failed to save theme preference", exception)
                    _themeState.update {
                        it.copy(isLoading = false, error = "Failed to save theme preference")
                    }
                }
            )
        }
    }

    fun onDarkModeChanged(isDark: Boolean) {
        val newThemeMode = if (isDark) ThemeModeUIModel.DARK else ThemeModeUIModel.LIGHT
        updateThemePreference(newThemeMode)
    }
}