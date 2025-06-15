package validators

import internal.NavigationCommand
import validator.NavigationValidator
import validator.ValidationResult

class CompositeNavigationValidator(
    private val validators: List<NavigationValidator>
) : NavigationValidator {

    override fun validate(command: NavigationCommand, currentRoute: String?): ValidationResult {
        for (validator in validators) {
            val result = validator.validate(command, currentRoute)
            if (result is ValidationResult.Invalid) {
                return result // Return first failure
            }
        }
        return ValidationResult.Valid
    }
}