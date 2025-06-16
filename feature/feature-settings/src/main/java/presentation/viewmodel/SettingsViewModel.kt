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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import language.SaveLanguagePreferenceUseCase
import model.LanguagePreference
import user.GetUserInfoUseCase
import user.LogoutUserUseCase

data class SettingsUiState(
    val isUserLoggedIn: Boolean = false,
    val userName: String? = null,
    val userEmail: String? = null,
    val isLoading: Boolean = true,
    val showLogoutConfirmationDialog: Boolean = false
)

class SettingsViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase, // Added
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
        //appNavigator.navigate(NavigationCommand.To(AppDestination.Home))
        logger.logDebug("Account info clicked", tag = TAG)
    }

    fun onLanguageSettingsClicked() {
        viewModelScope.launch {
            // Instead of direct navigation, emit an event for the UI to observe
            _navigateToLanguageSelection.emit(Unit)
            logger.logDebug("Language settings clicked, attempting to navigate.", tag = TAG)
            // The old navigationChannel call can be removed or kept if it serves another purpose
            // For this specific navigation, the SharedFlow is preferred to decouple ViewModel from NavController.
            // navigationChannel.trySend(NavigationCommand.To(AppDestination.LanguageSelection))
        }
    }

    fun confirmLanguageChange(languageCode: String) {
        viewModelScope.launch {
            logger.logDebug("confirmLanguageChange called with languageCode: $languageCode", tag = TAG)
            val result = saveLanguagePreferenceUseCase(LanguagePreference(languageCode))

            if (result.isSuccess) {
                logger.logDebug("Language preference updated to $languageCode, navigating to Home.", tag = TAG)
                navigationChannel.trySend(NavigationCommand.To(AppDestination.Home))
            } else {
                logger.logError("Failed to update language preference to $languageCode: ${result.exceptionOrNull()?.message}", tag = TAG)
                // Optionally, communicate this error to the UI, e.g., via a SharedFlow<String> for error messages
            }
        }
    }
}