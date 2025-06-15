package di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import presentation.viewmodel.AppThemeViewModel
import presentation.viewmodel.SettingsViewModel

val featureSettingsModule: Module = module {

    viewModel {
        AppThemeViewModel(
            getThemePreferenceUseCase = get(),
            saveThemePreferenceUseCase = get(),
            logger = get()
        )
    }

    viewModel {
        SettingsViewModel(
            getUserInfoUseCase = get(),
            logoutUserUseCase = get(),
            navigationChannel = get(),
            logger = get()
        )
    }
}
