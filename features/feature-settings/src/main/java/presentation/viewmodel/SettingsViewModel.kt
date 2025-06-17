package presentation.viewmodel

import GenericLogger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import internal.AppDestination
import internal.NavigationCommand
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import language.GetLanguagePreferenceUseCase
import user.GetUserInfoUseCase
import user.LogoutUserUseCase

data class SettingsUiState(
    val isUserLoggedIn: Boolean = false,
    val userName: String? = null,
    val userEmail: String? = null,
    val isLoading: Boolean = true,
    val currentLanguageCode: String = "en",
    val showLogoutConfirmationDialog: Boolean = false
)

class SettingsViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase,
    private val navigationChannel: SendChannel<NavigationCommand>,
    private val logger: GenericLogger
) : ViewModel() {

    private val TAG = "SettingsViewModel"
    private val _settingsState = MutableStateFlow(SettingsUiState())
    val settingsState: StateFlow<SettingsUiState> = _settingsState.asStateFlow()

    // For signaling navigation to LanguageSelectionScreen
    private val _navigateToLanguageSelection = MutableSharedFlow<Unit>()
    val navigateToLanguageSelection: SharedFlow<Unit> = _navigateToLanguageSelection.asSharedFlow()

    init {
        logger.logDebug("ViewModel initialized. Instance: $this", tag = TAG)
        loadInitialSettings()
        observeLanguagePreference()
    }

    private fun observeLanguagePreference() {
        viewModelScope.launch {
            _settingsState.update { it.copy(isLoading = true) }
            getLanguagePreferenceUseCase().collectLatest { preference ->
                _settingsState.update { currentState ->
                    currentState.copy(
                        currentLanguageCode = preference.languageCode,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadInitialSettings() {
        logger.logDebug("loadInitialSettings called", tag = TAG)
        viewModelScope.launch {
            _settingsState.update { it.copy(isLoading = true) }

            val userResult = try {
                getUserInfoUseCase()
            } catch (e: Exception) {
                logger.logError("Error loading user info: ${e.message}", tag = TAG)
                Result.failure(e)
            }

            val user = userResult.getOrNull()

            _settingsState.update { currentState ->
                currentState.copy(
                    isUserLoggedIn = user != null,
                    userName = user?.userName,
                    userEmail = user?.email,
                    isLoading = false
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        logger.logDebug("ViewModel cleared.", tag = TAG)
    }

    fun onLogoutConfirmationRequested() {
        _settingsState.update { it.copy(showLogoutConfirmationDialog = true) }
    }

    fun onLogoutDialogDismissed() {
        _settingsState.update { it.copy(showLogoutConfirmationDialog = false) }
    }

    fun onLogoutConfirmed() {
        viewModelScope.launch {
            try {
                val logoutResult = logoutUserUseCase()
                if (logoutResult.isSuccess) {
                    _settingsState.update {
                        it.copy(
                            isUserLoggedIn = false,
                            userName = null,
                            userEmail = null
                        )
                    }
                    logger.logDebug("Logout successful", tag = TAG)
                    navigationChannel.trySend(NavigationCommand.To(AppDestination.Home))
                } else {
                    logger.logError("Logout failed: ${logoutResult.exceptionOrNull()?.message}", tag = TAG)
                }
            } catch (e: Exception) {
                logger.logError("Logout failed: ${e.message}", tag = TAG)
            } finally {
                _settingsState.update { it.copy(showLogoutConfirmationDialog = false) }
            }
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            navigationChannel.trySend(NavigationCommand.To(AppDestination.Login))
            logger.logDebug("Login clicked, navigating to Login screen.", tag = TAG)
        }
    }

    fun onAccountInfoClicked() {
        navigationChannel.trySend(NavigationCommand.To(AppDestination.Home))
        logger.logDebug("Account info clicked", tag = TAG)
    }

    fun onLanguageSettingsClicked() {
        viewModelScope.launch {
            _navigateToLanguageSelection.emit(Unit)
            logger.logDebug("Language settings clicked, attempting to navigate.", tag = TAG)
        }
    }
}