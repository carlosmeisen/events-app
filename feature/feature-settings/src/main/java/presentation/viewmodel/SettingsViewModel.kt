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
import user.GetUserInfoUseCase
import user.LogoutUserUseCase

data class SettingsUiState(
    val isUserLoggedIn: Boolean = false,
    val userName: String? = null,
    val userEmail: String? = null,
    val isLoading: Boolean = true
)

class SettingsViewModel(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
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

    fun onLogoutClicked() {
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
                    //navigationChannel.trySend(NavigationCommand.To(AppDestination.Home))
                    logger.logDebug("Logout successful", tag = TAG)
                } else {
                    logger.logError("Logout failed: ${logoutResult.exceptionOrNull()?.message}", tag = TAG)
                }
            } catch (e: Exception) {
                logger.logError("Logout failed: ${e.message}", tag = TAG)
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
}