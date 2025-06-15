package theme

import kotlinx.coroutines.flow.Flow
import preference.ThemeMode

interface GetThemePreferenceUseCase {
    operator fun invoke(): Flow<ThemeMode>
}