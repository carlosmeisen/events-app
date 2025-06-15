package com.example.festiveapp.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * Extension property to provide a DataStore instance for theme preferences.
 * This sets up the DataStore with the specified name "theme_preferences".
 */
val Context.appThemeDataStore by preferencesDataStore(name = "theme_preferences")