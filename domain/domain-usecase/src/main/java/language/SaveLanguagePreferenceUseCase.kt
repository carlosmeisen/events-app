package language

import model.LanguagePreference

interface SaveLanguagePreferenceUseCase {
    suspend operator fun invoke(preference: LanguagePreference): Result<Unit>
}