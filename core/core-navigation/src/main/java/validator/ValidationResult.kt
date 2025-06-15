package validator

import internal.NavigationCommand

sealed class ValidationResult(val reason: String? = null) {
    data object Valid : ValidationResult()
    data class Invalid(
        val message: String,
        val fallbackCommand: NavigationCommand? = null
    ) : ValidationResult(reason = message)
}