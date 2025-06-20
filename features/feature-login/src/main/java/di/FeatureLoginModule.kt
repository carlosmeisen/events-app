package di

import presentation.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureLoginModule = module {
    viewModel { LoginViewModel() }
}
