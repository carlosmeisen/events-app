package external

/**
 * Interface for launching external Android applications.
 * This abstraction decouples the AppNavigator from Android's Intent system.
 */
interface ExternalAppLauncher {
    /**
     * Launches an external application based on the provided [ExternalAppData].
     * @param externalAppData The data describing the external app to launch.
     */
    fun launchApp(externalAppData: ExternalAppData)
}