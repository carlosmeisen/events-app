package validators

import destinations.DestinationRegistry
import internal.AppDestination
import internal.NavigationCommand
import validator.NavigationValidator
import validator.ValidationResult

class BasicNavigationValidator : NavigationValidator {

    private val navigationHistory = mutableListOf<String>()
    private val maxHistorySize = 10

    override fun validate(command: NavigationCommand, currentRoute: String?): ValidationResult {
        return when (command) {
            is NavigationCommand.To -> validateDestination(command.destination.route)
            is NavigationCommand.ToRoute -> validateRoute(command.route, currentRoute)
            is NavigationCommand.PopUpTo -> validatePopUpTo(command.route)
            NavigationCommand.Up, NavigationCommand.PopBackStack -> ValidationResult.Valid
            is NavigationCommand.OpenExternalApp -> ValidationResult.Valid // Basic validation
        }
    }

    private fun validateDestination(route: String): ValidationResult {
        return if (DestinationRegistry.isValidDestination(route)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(
                message = "Unknown destination: $route",
                fallbackCommand = NavigationCommand.To(AppDestination.Home)
            )
        }
    }

    private fun validateRoute(route: String, currentRoute: String?): ValidationResult {
        if (!DestinationRegistry.isValidDestination(route)) {
            return ValidationResult.Invalid(
                message = "Invalid route: $route",
                fallbackCommand = NavigationCommand.To(AppDestination.Home)
            )
        }

        if (currentRoute == route) {
            return ValidationResult.Invalid(
                message = "Cannot navigate to the same route: $route"
            )
        }

        if (detectNavigationLoop(route)) {
            return ValidationResult.Invalid(
                message = "Navigation loop detected for route: $route",
                fallbackCommand = NavigationCommand.To(AppDestination.Home)
            )
        }

        updateNavigationHistory(route)
        return ValidationResult.Valid
    }

    private fun validatePopUpTo(route: String): ValidationResult {
        return if (DestinationRegistry.isValidDestination(route)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(message = "PopUpTo target route does not exist: $route")
        }
    }

    private fun detectNavigationLoop(route: String): Boolean {
        val recentOccurrences = navigationHistory.takeLast(5).count { it == route }
        return recentOccurrences >= 3
    }

    private fun updateNavigationHistory(route: String) {
        navigationHistory.add(route)
        if (navigationHistory.size > maxHistorySize) {
            navigationHistory.removeAt(0)
        }
    }
}