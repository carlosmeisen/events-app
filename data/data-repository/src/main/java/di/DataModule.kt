package di

import org.koin.dsl.module
import preference.ThemePreferenceRepository
import theme.ThemePreferenceRepositoryImpl
import user.UserRepository
import user.UserRepositoryImpl

val dataModule = module {
    single<ThemePreferenceRepository> {
        ThemePreferenceRepositoryImpl(dataStore = get())
    }
    single<UserRepository> {
        UserRepositoryImpl()
    }

}