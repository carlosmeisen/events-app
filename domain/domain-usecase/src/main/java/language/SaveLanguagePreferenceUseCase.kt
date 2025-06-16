package language

import model.LanguagePreference
import repository.UserPreferenceRepository

class SaveLanguagePreferenceUseCase(
    private val userPreferenceRepository: UserPreferenceRepository
) {
    suspend operator fun invoke(preference: LanguagePreference): Result<Unit> {
        return try {
            userPreferenceRepository.saveLanguagePreference(preference)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
