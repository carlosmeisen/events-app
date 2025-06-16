package language

import kotlinx.coroutines.flow.Flow
import model.LanguagePreference
import preference.UserPreferenceRepository

class GetLanguagePreferenceUseCaseImpl(
    private val userPreferenceRepository: UserPreferenceRepository
) : GetLanguagePreferenceUseCase {
    override operator fun invoke(): Flow<LanguagePreference> =
        userPreferenceRepository.getLanguagePreference()
}
