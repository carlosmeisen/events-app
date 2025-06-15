package validator

import internal.NavigationCommand

interface NavigationValidator {
    fun validate(command: NavigationCommand, currentRoute: String?): ValidationResult
}