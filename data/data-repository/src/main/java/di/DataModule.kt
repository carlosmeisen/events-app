package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import datastore.provideThemeDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

// Domain Repository Interfaces
import preference.ThemePreferenceRepository
import repository.UserPreferenceRepository
import user.UserRepository

// Implementation Classes from this Data Layer
import theme.ThemePreferenceRepositoryImpl
import preference.UserPreferenceRepositoryImpl
import user.UserRepositoryImpl


val dataModule = module {

    // Provide the DataStore<Preferences> instance
    single<DataStore<Preferences>> {
        androidContext().provideThemeDataStore
    }

    single<ThemePreferenceRepository> {
        ThemePreferenceRepositoryImpl(dataStore = get())
    }

    single<UserPreferenceRepository> {
        UserPreferenceRepositoryImpl(dataStore = get())
    }

    single<UserRepository> {
        UserRepositoryImpl()
    }
}