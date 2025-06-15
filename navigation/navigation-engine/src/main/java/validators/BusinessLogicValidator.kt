package validators

import internal.AppDestination
import internal.NavigationCommand
import providers.AuthProvider
import providers.NetworkProvider
import validator.NavigationValidator
import validator.ValidationResult

class BusinessLogicValidator(
    private val authProvider: AuthProvider, // Injected dependency
    private val networkProvider: NetworkProvider // Injected dependency
) : NavigationValidator {

    private val navigationRules = mapOf(
        AppDestination.Settings.route to setOf(
            AppDestination.Home.route,
            AppDestination.Calendar.route
        )
    )

    private val authRequiredRoutes = setOf(
        AppDestination.Favorites.route
    )

    private val networkRequiredRoutes = setOf(
        AppDestination.Calendar.route
    )

    override fun validate(command: NavigationCommand, currentRoute: String?): ValidationResult {
        val targetRoute = when (command) {
            is NavigationCommand.To -> command.destination.route
            is NavigationCommand.ToRoute -> command.route
            else -> return ValidationResult.Valid
        }

//        // Check navigation constraints
//        navigationRules[targetRoute]?.let { allowedSources ->
//            if (allowedSources.isNotEmpty() && currentRoute !in allowedSources) {
//                return ValidationResult.Invalid(
//                    message = "Cannot navigate to $targetRoute from $currentRoute",
//                    fallbackCommand = NavigationCommand.To(AppDestination.Home)
//                )
//            }
//        }
//
//        // Check authentication
//        if (targetRoute in authRequiredRoutes && !authProvider.isAuthenticated()) {
//            return ValidationResult.Invalid(
//                message = "Authentication required for $targetRoute",
//                fallbackCommand = NavigationCommand.To(AppDestination.Home)
//            )
//        }
//
//        // Check network
//        if (targetRoute in networkRequiredRoutes && !networkProvider.isAvailable()) {
//            return ValidationResult.Invalid(
//                message = "Network required for $targetRoute"
//            )
//        }

        return ValidationResult.Valid
    }
}