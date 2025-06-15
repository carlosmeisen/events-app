package orchestrator

import androidx.lifecycle.LifecycleCoroutineScope

interface NavigationOrchestrator {
    /**
     * Starts listening for navigation commands on the channel.
     * @param lifecycleScope The [androidx.lifecycle.LifecycleCoroutineScope] to launch the collection coroutine in.
     */
    fun startListening(lifecycleScope: LifecycleCoroutineScope)
}