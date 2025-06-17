package di

import appinitialization.InitializeAppUseCase
import appinitialization.InitializeAppUseCaseImpl
import language.ApplyAppLanguageUseCase
import language.ApplyAppLanguageUseCaseImpl
import language.GetAvailableLanguagesUseCase
import language.GetAvailableLanguagesUseCaseImpl
import language.GetLanguagePreferenceUseCase
import language.GetLanguagePreferenceUseCaseImpl
import language.SaveLanguagePreferenceUseCase
import language.SaveLanguagePreferenceUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import theme.GetThemePreferenceUseCase
import theme.GetThemePreferenceUseCaseImpl
import theme.SaveThemePreferenceUseCase
import theme.SaveThemePreferenceUseCaseImpl
import user.GetUserInfoUseCase
import user.GetUserInfoUseCaseImpl
import user.LogoutUserUseCase
import user.LogoutUserUseCaseImpl

val domainUseCaseModule = module {
    factory<GetThemePreferenceUseCase> { GetThemePreferenceUseCaseImpl(themePreferenceRepository = get()) }
    factory<SaveThemePreferenceUseCase> { SaveThemePreferenceUseCaseImpl(themePreferenceRepository = get()) }
    factory<GetUserInfoUseCase> { GetUserInfoUseCaseImpl(userRepository = get()) }
    factory<LogoutUserUseCase> { LogoutUserUseCaseImpl(userRepository = get()) }
    factory<GetLanguagePreferenceUseCase> {
        GetLanguagePreferenceUseCaseImpl(
            userPreferencesRepository = get()
        )
    }
    factory<SaveLanguagePreferenceUseCase> {
        SaveLanguagePreferenceUseCaseImpl(
            userPreferencesRepository = get()
        )
    }
    factory<GetAvailableLanguagesUseCase> {
        GetAvailableLanguagesUseCaseImpl(
            languageConfigRepository = get()
        )
    }

    factory<ApplyAppLanguageUseCase> {
        ApplyAppLanguageUseCaseImpl(applicationContext = androidApplication())
    }

    factory<InitializeAppUseCase> {
        InitializeAppUseCaseImpl(
            userPreferencesRepository = get()
        )
    }
}
