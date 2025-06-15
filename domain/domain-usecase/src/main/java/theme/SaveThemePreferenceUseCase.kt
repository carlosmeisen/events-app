package theme

import preference.ThemeMode

interface SaveThemePreferenceUseCase {
    suspend operator fun invoke(themeMode: ThemeMode): Result<Unit>
}