package com.example.feature_login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginResultMessage = MutableStateFlow<String?>(null)
    val loginResultMessage: StateFlow<String?> = _loginResultMessage

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
        _loginResultMessage.value = null // Clear message on input change
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _loginResultMessage.value = null // Clear message on input change
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _isLoading.value = true
            _loginResultMessage.value = null
            delay(1000) // Simulate network request

            if (username.value == "a" && password.value == "a") {
                _loginResultMessage.value = "Login Successful!"
            } else {
                _loginResultMessage.value = "Error: Invalid username or password."
            }
            _isLoading.value = false
        }
    }
}
