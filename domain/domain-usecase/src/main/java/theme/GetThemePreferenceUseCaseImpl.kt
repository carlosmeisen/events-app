package theme

import kotlinx.coroutines.flow.Flow
import preference.ThemeMode
import preference.ThemePreferenceRepository

class GetThemePreferenceUseCaseImpl(
    private val themePreferenceRepository: ThemePreferenceRepository
) : GetThemePreferenceUseCase {
    override operator fun invoke(): Flow<ThemeMode> {
        return themePreferenceRepository.getThemePreference()
    }
}