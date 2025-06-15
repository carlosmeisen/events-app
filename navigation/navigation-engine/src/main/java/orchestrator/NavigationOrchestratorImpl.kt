package orchestrator

import androidx.lifecycle.LifecycleCoroutineScope
import internal.AppNavigator
import internal.NavigationCommand
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Orchestrates navigation by listening to a channel of [internal.NavigationCommand]s
 * and executing them using an [internal.AppNavigator].
 * This decouples the navigation logic from specific UI components.
 */
class NavigationOrchestratorImpl(
    private val navigationChannel: Channel<NavigationCommand>,
    private val navigator: AppNavigator
) : NavigationOrchestrator {
    /**
     * Starts listening for navigation commands on the channel.
     * @param lifecycleScope The [androidx.lifecycle.LifecycleCoroutineScope] to launch the collection coroutine in.
     */
    override fun startListening(lifecycleScope: LifecycleCoroutineScope) {
        lifecycleScope.launch {
            navigationChannel.receiveAsFlow().collect { command ->
                navigator.navigate(command = command)
            }
        }
    }
}