package internal

/**
 * Interface for navigating within the app's Compose graph and launching external apps.
 * This abstraction allows UI components to trigger navigation without direct knowledge
 * of the NavController or Android Intent system.
 */
interface AppNavigator {
    /**
     * Executes a navigation command.
     * @param command The [NavigationCommand] to execute.
     */
    fun navigate(command: NavigationCommand)
}
