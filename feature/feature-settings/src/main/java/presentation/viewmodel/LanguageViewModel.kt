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
import language.GetLanguagePreferenceUseCase
import language.SaveLanguagePreferenceUseCase
import model.LanguagePreference

data class LanguageState(
    val currentLanguageCode: String = "en", // Default to English
    val isLoading: Boolean = false,
    val error: String? = null
)

class LanguageViewModel(
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase,
    private val saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase,
    private val logger: GenericLogger // Assuming GenericLogger is available and injected
) : ViewModel() {

    private val _languageState = MutableStateFlow(LanguageState())
    val languageState: StateFlow<LanguageState> = _languageState.asStateFlow()

    init {
        observeLanguagePreference()
    }

    private fun observeLanguagePreference() {
        viewModelScope.launch {
            _languageState.update { it.copy(isLoading = true) }
            getLanguagePreferenceUseCase().collectLatest { preference ->
                _languageState.update { currentState ->
                    currentState.copy(
                        currentLanguageCode = preference.languageCode,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onLanguageSelected(languageCode: String) {
        viewModelScope.launch {
            _languageState.update { it.copy(isLoading = true, error = null) }
            val result = saveLanguagePreferenceUseCase(LanguagePreference(languageCode))
            result.fold(
                onSuccess = {
                    // State will be updated by observeLanguagePreference
                    // Trigger for locale update will be added later
                    logger.logInfo("LanguageViewModel", "Language preference saved: $languageCode")
                },
                onFailure = { exception ->
                    logger.logError("LanguageViewModel", "Failed to save language", exception)
                    _languageState.update {
                        it.copy(isLoading = false, error = "Failed to save language preference")
                    }
                }
            )
        }
    }
}
