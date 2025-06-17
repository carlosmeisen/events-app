package appinitialization

import kotlinx.coroutines.flow.firstOrNull
import preference.UserPreferencesRepository

class InitializeAppUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository,
) : InitializeAppUseCase {
    override suspend operator fun invoke(): Result<InitializationData> {
        return try {
            val languagePreference =
                userPreferencesRepository.getLanguagePreference().firstOrNull()?.languageCode

            val actions = buildList {
                if (languagePreference != null) {
                    add(InitializationAction.LoadUserPreferences(languagePreference))
                }
            }
            val initData = InitializationData(
                actions = actions
            )

            Result.success(initData)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}