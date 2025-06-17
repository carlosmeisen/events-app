package com.example.festiveapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import datastore.appThemeDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for DataStore preferences.
 * Provides a singleton instance of [DataStore<Preferences>].
 */
val appDataStoreModule = module {
    single<DataStore<Preferences>> {
        androidContext().appThemeDataStore // Use androidContext() to get the Context
    }
}