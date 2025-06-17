package language

import model.LanguagePreference
import preference.UserPreferencesRepository

class SaveLanguagePreferenceUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : SaveLanguagePreferenceUseCase {
    override suspend operator fun invoke(preference: LanguagePreference): Result<Unit> {
        return try {
            userPreferencesRepository.saveLanguagePreference(preference)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
