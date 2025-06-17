package preference

import kotlinx.coroutines.flow.Flow
import model.LanguagePreference

interface UserPreferencesRepository {
    fun getLanguagePreference(): Flow<LanguagePreference>
    suspend fun saveLanguagePreference(preference: LanguagePreference)
}