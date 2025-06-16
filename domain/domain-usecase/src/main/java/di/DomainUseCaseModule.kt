package di

import language.GetLanguagePreferenceUseCase
import language.SaveLanguagePreferenceUseCase
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
    factory { GetLanguagePreferenceUseCase(userPreferenceRepository = get()) }
    factory { SaveLanguagePreferenceUseCase(userPreferenceRepository = get()) }
}
