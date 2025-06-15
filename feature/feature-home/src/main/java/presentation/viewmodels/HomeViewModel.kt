package presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import internal.NavigationCommand

class HomeViewModel(private val navigationChannel: Channel<NavigationCommand>) : ViewModel() {
}