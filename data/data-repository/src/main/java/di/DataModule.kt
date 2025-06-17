package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import datastore.appThemeDataStore
import language.LanguageConfigRepository
import language.LanguageConfigRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

// Domain Repository Interfaces
import preference.ThemePreferenceRepository
import preference.UserPreferencesRepository
import user.UserRepository

// Implementation Classes from this Data Layer
import theme.ThemePreferenceRepositoryImpl
import preference.UserPreferencesRepositoryImpl
import user.UserRepositoryImpl


val dataModule = module {

    // Provide the DataStore<Preferences> instance
    single<DataStore<Preferences>> {
        androidContext().appThemeDataStore
    }

    single<ThemePreferenceRepository> {
        ThemePreferenceRepositoryImpl(dataStore = get())
    }

    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl(dataStore = get())
    }

    single<UserRepository> {
        UserRepositoryImpl()
    }

    single<LanguageConfigRepository> {
        LanguageConfigRepositoryImpl()
    }
}