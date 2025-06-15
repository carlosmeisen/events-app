package theme

import preference.ThemeMode
import preference.ThemePreferenceRepository

class SaveThemePreferenceUseCaseImpl(
    private val themePreferenceRepository: ThemePreferenceRepository
) : SaveThemePreferenceUseCase {
    override suspend operator fun invoke(themeMode: ThemeMode): Result<Unit> {
        return themePreferenceRepository.saveThemePreference(themeMode)
    }
}