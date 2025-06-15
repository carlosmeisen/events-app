package di

import external.ExternalAppLauncher
import internal.AppNavigator
import AppNavigatorImpl
import androidx.navigation.NavHostController
import backstack.BackStackTracker
import internal.NavigationCommand
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import orchestrator.NavigationOrchestrator
import orchestrator.NavigationOrchestratorImpl
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import validator.NavigationValidator
import validators.BasicNavigationValidator
import validators.BusinessLogicValidator
import validators.NavigationValidatorImpl
import validators.CompositeNavigationValidator

val navigationModule = module {

    // Navigation Channel
    single<Channel<NavigationCommand>> {
        Channel(Channel.BUFFERED)
    }

    single<SendChannel<NavigationCommand>> {
        get<Channel<NavigationCommand>>() // Get the already defined full Channel
    }

    // Basic Navigation Validator
    single<NavigationValidator>(named("basic")) {
        BasicNavigationValidator()
    }

    // Business Logic Validator
    single<NavigationValidator>(named("business")) {
        BusinessLogicValidator(
            authProvider = get(),
            networkProvider = get()
        )
    }

    // Compose Navigation Validator (factory since it needs NavController)
    factory<NavigationValidator>(named("compose")) { parameters ->
        val navController = parameters.get<NavHostController>()
        val backStackTracker = BackStackTracker()
        NavigationValidatorImpl(navController, backStackTracker)
    }

    // Composite Navigation Validator
    factory<NavigationValidator> { parameters ->
        val navController = parameters.get<NavHostController>()
        CompositeNavigationValidator(
            validators = listOf(
                get<NavigationValidator>(named("basic")),
                get<NavigationValidator>(named("business")),
                get<NavigationValidator>(named("compose")) { parametersOf(navController) }
            )
        )
    }

    // App Navigator
    factory<AppNavigator> { parameters ->
        val navController = parameters.get<NavHostController>()
        val validator = get<NavigationValidator> { parametersOf(navController) }
        AppNavigatorImpl(
            navController = navController,
            validator = validator,
            externalAppLauncher = get<ExternalAppLauncher>()
        )
    }

    // Navigation Orchestrator
    factory<NavigationOrchestrator> { parameters ->
        val navController = parameters.get<NavHostController>()
        NavigationOrchestratorImpl(
            navigationChannel = get(),
            navigator = get<AppNavigator> { parametersOf(navController) }
        )
    }
}