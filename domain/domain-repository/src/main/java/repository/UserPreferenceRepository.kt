package repository

import kotlinx.coroutines.flow.Flow
import model.LanguagePreference

interface UserPreferenceRepository {
    fun getLanguagePreference(): Flow<LanguagePreference>
    suspend fun saveLanguagePreference(preference: LanguagePreference)
}
