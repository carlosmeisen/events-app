package validators

import androidx.navigation.NavHostController
import backstack.BackStackTracker
import internal.AppDestination
import internal.NavigationCommand
import validator.NavigationValidator
import validator.ValidationResult

class NavigationValidatorImpl(
    private val navController: NavHostController,
    private val backStackTracker: BackStackTracker
) : NavigationValidator {

    override fun validate(command: NavigationCommand, currentRoute: String?): ValidationResult {
        return when (command) {
            NavigationCommand.Up -> validateNavigateUp()
            NavigationCommand.PopBackStack -> validatePopBackStack()
            is NavigationCommand.PopUpTo -> validatePopUpTo(command.route, command.inclusive)
            is NavigationCommand.To -> validateNavigateTo(command.destination, currentRoute)
            else -> ValidationResult.Valid
        }
    }

    private fun validateNavigateUp(): ValidationResult {
        return if (navController.previousBackStackEntry != null) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(
                message = "Cannot navigate up - no previous destination",
                fallbackCommand = NavigationCommand.To(AppDestination.Home)
            )
        }
    }

    private fun validatePopBackStack(): ValidationResult {
        return if (backStackTracker.size() > 1) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(
                message = "Cannot pop back stack - at root destination",
                fallbackCommand = null // Stay where we are
            )
        }
    }

    private fun validatePopUpTo(route: String, inclusive: Boolean = false): ValidationResult {
        return if (backStackTracker.contains(route)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(
                message = "PopUpTo route '$route' not found in back stack",
                fallbackCommand = NavigationCommand.To(AppDestination.Home)
            )
        }
    }

    private fun validateNavigateTo(destination: AppDestination, currentRoute: String?): ValidationResult {
        // Example business logic validations
        return when (destination) {
            AppDestination.Settings -> validateSettingsAccess(currentRoute)
            AppDestination.Favorites -> validateFavoritesAccess(currentRoute)
            else -> ValidationResult.Valid
        }
    }

    private fun validateSettingsAccess(currentRoute: String?): ValidationResult {
        // Example: Settings only accessible from Home or Calendar
        val allowedRoutes = setOf(AppDestination.Home.route, AppDestination.Calendar.route)

        return if (currentRoute in allowedRoutes) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(
                message = "Settings not accessible from current screen",
                fallbackCommand = NavigationCommand.To(AppDestination.Home)
            )
        }
    }

    private fun validateFavoritesAccess(currentRoute: String?): ValidationResult {
        // Add your business logic here
        return ValidationResult.Valid
    }
}