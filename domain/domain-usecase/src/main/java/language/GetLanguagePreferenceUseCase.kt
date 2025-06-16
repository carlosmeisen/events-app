package language

import kotlinx.coroutines.flow.Flow
import model.LanguagePreference
import repository.UserPreferenceRepository

class GetLanguagePreferenceUseCase(
    private val userPreferenceRepository: UserPreferenceRepository
) {
    operator fun invoke(): Flow<LanguagePreference> = userPreferenceRepository.getLanguagePreference()
}
