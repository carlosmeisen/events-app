package language

import kotlinx.coroutines.flow.Flow
import model.LanguagePreference

interface GetLanguagePreferenceUseCase {
    operator fun invoke(): Flow<LanguagePreference>
}