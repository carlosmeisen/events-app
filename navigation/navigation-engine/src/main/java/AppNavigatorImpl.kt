import androidx.navigation.NavController
import androidx.navigation.NavHostController
import external.ExternalAppLauncher
import internal.AppNavigator
import internal.NavigationCommand
import validator.NavigationValidator
import validator.ValidationResult

/**
 * Implementation of [AppNavigator] using Jetpack Compose's [NavController].
 * This class handles both internal Compose navigation and delegates external app launches.
 *
 * @param navController The [NavController] instance that manages the navigation within the Compose graph.
 * @param externalAppLauncher The [ExternalAppLauncher] responsible for opening external applications.
 */

class AppNavigatorImpl(
    private val navController: NavHostController,
    private val validator: NavigationValidator? = null,
    private val externalAppLauncher: ExternalAppLauncher
) : AppNavigator {

    override fun navigate(command: NavigationCommand) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        // Validate the command if validator is available
        val validationResult = validator?.validate(command, currentRoute) ?: ValidationResult.Valid

        when (validationResult) {
            is ValidationResult.Valid -> executeCommand(command)
            is ValidationResult.Invalid -> {
                // Log the validation failure
                println("Navigation blocked: ${validationResult.message}")

                // Execute fallback command if provided
                validationResult.fallbackCommand?.let { fallback ->
                    executeCommand(fallback)
                }
            }
        }
    }

    private fun executeCommand(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.To -> {
                navController.navigate(command.destination.route) {
                    command.popUpToRoute?.let { popUpTo(it) }
                    launchSingleTop = command.singleTop
                }
            }
            NavigationCommand.Up -> navController.navigateUp()
            NavigationCommand.PopBackStack -> navController.popBackStack()
            is NavigationCommand.PopUpTo -> {
                navController.popBackStack(command.route, command.inclusive)
            }

            is NavigationCommand.OpenExternalApp -> externalAppLauncher.launchApp(command.data)
            is NavigationCommand.ToRoute -> {
                navController.navigate(command.route) {
                    command.popUpToRoute?.let { popUpTo(it) }
                    launchSingleTop = command.singleTop
                }
            }
        }
    }
}