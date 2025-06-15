package theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import preference.ThemeMode
import preference.ThemePreferenceRepository

private object PreferencesKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
}

class ThemePreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ThemePreferenceRepository {

    override fun getThemePreference(): Flow<ThemeMode> {
        return dataStore.data.map { preferences ->
            val themeName = preferences[PreferencesKeys.THEME_MODE]
                ?: ThemeMode.SYSTEM_DEFAULT.name // Default to SYSTEM
            try {
                ThemeMode.valueOf(themeName)
            } catch (e: Exception) {
                ThemeMode.SYSTEM_DEFAULT
            }
        }
    }

    override suspend fun saveThemePreference(themeMode: preference.ThemeMode): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.THEME_MODE] = themeMode.name
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}