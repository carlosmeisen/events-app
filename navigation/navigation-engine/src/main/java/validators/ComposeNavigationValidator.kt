package validators

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import backstack.BackStackTracker
import internal.NavigationCommand
import statemanager.NavigationStateManager
import validator.NavigationValidator
import validator.ValidationResult

// CompositionLocal for the validator
val LocalNavigationValidator = staticCompositionLocalOf<NavigationValidator?> { null }

@Composable
fun ComposeNavigationValidator(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    // Create and remember the back stack tracker
    val backStackTracker = remember { BackStackTracker() }

    // Create the validator
    val validator = remember(navController, backStackTracker) {
        NavigationValidatorImpl(navController, backStackTracker)
    }

    // Provide validator through Composition Local
    CompositionLocalProvider(LocalNavigationValidator provides validator) {
        NavigationStateManager(
            navController = navController,
            backStackTracker = backStackTracker
        ) {
            content()
        }
    }
}

// Extension function to make validation easy to use anywhere in your composable tree
@Composable
fun validateNavigation(command: NavigationCommand, currentRoute: String? = null): ValidationResult {
    val validator = LocalNavigationValidator.current
    return validator?.validate(command, currentRoute) ?: ValidationResult.Valid
}