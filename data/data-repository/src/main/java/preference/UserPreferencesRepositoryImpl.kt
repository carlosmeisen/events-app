package preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import model.LanguagePreference
import preference.AppPreferencesKeys.DEFAULT_LANGUAGE_CODE
import preference.UserPreferencesRepositoryImpl.PreferencesKeys.LANGUAGE_CODE_KEY
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        // Updated to use the constant from AppPreferencesKeys
        val LANGUAGE_CODE_KEY = stringPreferencesKey(AppPreferencesKeys.KEY_LANGUAGE_CODE)
    }

    override fun getLanguagePreference(): Flow<LanguagePreference> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    // Log error, potentially emit a default
                    emit(androidx.datastore.preferences.core.emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val languageCode = preferences[LANGUAGE_CODE_KEY] ?: DEFAULT_LANGUAGE_CODE
                LanguagePreference(languageCode)
            }
    }

    override suspend fun saveLanguagePreference(preference: LanguagePreference) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_CODE_KEY] = preference.languageCode
        }
    }
}