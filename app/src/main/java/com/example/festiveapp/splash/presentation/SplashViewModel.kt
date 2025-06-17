package com.example.festiveapp.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import appinitialization.InitializationAction
import appinitialization.InitializeAppUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import locale.LocaleService
import preference.UserPreferencesRepository

data class SplashState(
    val isLoading: Boolean = true,
    val isInitializationComplete: Boolean = false,
    val error: String? = null
)

class SplashViewModel(
    private val initializeAppUseCase: InitializeAppUseCase,
    private val localeService: LocaleService
) : ViewModel() {

    private val _splashState = MutableStateFlow(SplashState())
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()

    fun initializeApp() {
        viewModelScope.launch {
            val result = initializeAppUseCase()

            result.fold(
                onSuccess = { initData ->
                    initData.actions.forEach { action ->

                        when (action) {
                            is InitializationAction.LoadUserPreferences -> {
                                if (localeService.getCurrentLocale() != action.languageCode) {
                                    localeService.updateLocale(action.languageCode)
                                }
                            }

                            else -> {
                                // Handle other actions if needed
                            }
                        }
                    }

                    _splashState.value = _splashState.value.copy(
                        isLoading = false,
                        isInitializationComplete = true
                    )
                },
                onFailure = { exception ->
                    _splashState.value = _splashState.value.copy(
                        isLoading = false,
                        isInitializationComplete = true,
                        error = exception.message
                    )
                }
            )
        }
    }
}