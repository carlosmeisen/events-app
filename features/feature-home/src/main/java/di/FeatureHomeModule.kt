package di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import presentation.viewmodels.HomeViewModel

val featureHomeModule: Module = module {
    viewModel { HomeViewModel(navigationChannel = get()) }
}