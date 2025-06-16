package language

import model.LanguagePreference
import preference.UserPreferenceRepository

class SaveLanguagePreferenceUseCaseImpl(
    private val userPreferenceRepository: UserPreferenceRepository
) : SaveLanguagePreferenceUseCase {
    override suspend operator fun invoke(preference: LanguagePreference): Result<Unit> {
        return try {
            userPreferenceRepository.saveLanguagePreference(preference)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
