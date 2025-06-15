package preference

import kotlinx.coroutines.flow.Flow

interface ThemePreferenceRepository {
    fun getThemePreference(): Flow<ThemeMode>
    suspend fun saveThemePreference(themeMode: ThemeMode): Result<Unit>
}