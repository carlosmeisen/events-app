package datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import preference.AppPreferencesKeys
import kotlinx.coroutines.flow.map

/**
 * Extension property to provide a DataStore instance for theme preferences.
 * This sets up the DataStore with the specified name "theme_preferences".
 */
val Context.appThemeDataStore by preferencesDataStore(name = "theme_preferences")

/**
 * Key for storing the selected language code in DataStore.
 */
val SELECTED_LANGUAGE_CODE = stringPreferencesKey(AppPreferencesKeys.KEY_LANGUAGE_CODE)

/**
 * Retrieves the selected language code from DataStore.
 *
 * @param context The application context.
 * @return The language code string (e.g., "en", "es-ES") or null if not found.
 */
suspend fun getSelectedLanguage(context: Context): String? {
    return context.appThemeDataStore.data.map { preferences ->
        preferences[SELECTED_LANGUAGE_CODE]
    }.firstOrNull()
}

/**
 * Saves the selected language code to DataStore.
 *
 * @param context The application context.
 * @param languageCode The language code string to save.
 */
suspend fun saveSelectedLanguage(context: Context, languageCode: String) {
    context.appThemeDataStore.edit { preferences ->
        preferences[SELECTED_LANGUAGE_CODE] = languageCode
    }
}
