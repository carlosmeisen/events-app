package preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.LanguagePreference
import repository.UserPreferenceRepository // Domain repository interface

// Note: The DataStore instance (provideThemeDataStore) is not directly used here.
// It will be injected into this class via DI.

class UserPreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences> // Injected DataStore
) : UserPreferenceRepository {

    private object PreferencesKeys {
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
        // Add other general user preference keys here if any in the future
    }

    override fun getLanguagePreference(): Flow<LanguagePreference> {
        return dataStore.data.map { preferences ->
            // Default to "en" if no language code is stored
            val languageCode = preferences[PreferencesKeys.LANGUAGE_CODE] ?: "en"
            LanguagePreference(languageCode)
        }
    }

    override suspend fun saveLanguagePreference(preference: LanguagePreference) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE_CODE] = preference.languageCode
        }
    }

    // If UserPreferenceRepository had other methods (e.g., for theme),
    // they would be implemented here, potentially sharing the same DataStore
    // or delegating to other specific preference stores if needed.
    // For now, it only has language methods as per the previous subtask.
}
