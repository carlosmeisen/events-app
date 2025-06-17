package presentation.viewmodel

import GenericLogger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import language.GetAvailableLanguagesUseCase
import language.GetLanguagePreferenceUseCase
import language.SaveLanguagePreferenceUseCase
import mapper.theme.toUIModel
import model.LanguagePreference
import preference.AppPreferencesKeys
import presentation.model.LanguageUIModel

data class LanguageState(
    val currentLanguageCode: String = AppPreferencesKeys.DEFAULT_LANGUAGE_CODE,
    val availableLanguages: List<LanguageUIModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class LanguageViewModel(
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase,
    private val saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase,
    private val getAvailableLanguagesUseCase: GetAvailableLanguagesUseCase,
    private val logger: GenericLogger
) : ViewModel() {

    private val TAG = "LanguageViewModel"

    private val _languageState = MutableStateFlow(LanguageState())
    val languageState: StateFlow<LanguageState> = _languageState.asStateFlow()

    private val _navigateToUp = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val navigateToUp: SharedFlow<Unit> = _navigateToUp.asSharedFlow()

    init {
        logger.logDebug("ViewModel initialized. Instance: $this", tag = TAG)
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _languageState.update { it.copy(isLoading = true, error = null) }

            try {
                // First get current language preference
                val currentLangPref = getLanguagePreferenceUseCase().first()
                logger.logDebug("Current language preference: ${currentLangPref.languageCode}", tag = TAG)

                // Then get available languages
                val availableLanguagesResult = getAvailableLanguagesUseCase()

                availableLanguagesResult.fold(
                    onSuccess = { languages ->
                        _languageState.update { currentState ->
                            currentState.copy(
                                currentLanguageCode = currentLangPref.languageCode,
                                availableLanguages = languages.toUIModel(currentLangPref.languageCode),
                                isLoading = false
                            )
                        }
                        logger.logDebug("Loaded initial data: CurrentLang=${currentLangPref.languageCode}, Available=${languages.size}", tag = TAG)

                        // Continue observing language preference changes
                        getLanguagePreferenceUseCase().collectLatest { preference ->
                            logger.logDebug("Observed language preference update: ${preference.languageCode}", tag = TAG)
                            _languageState.update { currentState ->
                                currentState.copy(
                                    currentLanguageCode = preference.languageCode,
                                    availableLanguages = currentState.availableLanguages.map { lang ->
                                        lang.copy(isSelected = lang.code == preference.languageCode)
                                    }
                                )
                            }
                        }
                    },
                    onFailure = { exception ->
                        logger.logError(TAG, "Failed to load available languages", exception)
                        _languageState.update {
                            it.copy(
                                currentLanguageCode = currentLangPref.languageCode,
                                isLoading = false,
                                error = "Failed to load language list"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                logger.logError(TAG, "Failed to load initial language data", e)
                _languageState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load language settings"
                    )
                }
            }
        }
    }

    fun changeLanguage(languageCode: String) {
        // Only proceed if the language is different from current
        if (languageCode == _languageState.value.currentLanguageCode) {
            return
        }

        viewModelScope.launch {
            val result = saveLanguagePreferenceUseCase(
                preference = LanguagePreference(languageCode = languageCode)
            )

            result.fold(
                onSuccess = {
                    logger.logInfo(TAG, "Language preference saved: $languageCode")
                    _navigateToUp.tryEmit(Unit)
                },
                onFailure = { exception ->
                    logger.logError(TAG, "Failed to save language", exception)
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        logger.logDebug("ViewModel cleared.", tag = TAG)
    }
}