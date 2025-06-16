package di

import language.GetLanguagePreferenceUseCase
import language.SaveLanguagePreferenceUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import presentation.viewmodel.AppThemeViewModel
import presentation.viewmodel.LanguageViewModel
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

    // New additions for Language
    factory { GetLanguagePreferenceUseCase(get()) } // Depends on UserPreferenceRepository
    factory { SaveLanguagePreferenceUseCase(get()) } // Depends on UserPreferenceRepository
    viewModel { LanguageViewModel(get(), get(), get()) } // Depends on UseCases and Logger
}
