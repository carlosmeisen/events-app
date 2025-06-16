package language

class GetAvailableLanguagesUseCaseImpl(
    private val languageConfigRepository: LanguageConfigRepository
) : GetAvailableLanguagesUseCase {
    override suspend fun invoke(): Result<List<LanguageOption>> {
        return languageConfigRepository.getAvailableLanguages()
    }
}