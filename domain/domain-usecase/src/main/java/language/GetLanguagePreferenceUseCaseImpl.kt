package language

import kotlinx.coroutines.flow.Flow
import model.LanguagePreference
import preference.UserPreferencesRepository

class GetLanguagePreferenceUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetLanguagePreferenceUseCase {
    override operator fun invoke(): Flow<LanguagePreference> =
        userPreferencesRepository.getLanguagePreference()
}
