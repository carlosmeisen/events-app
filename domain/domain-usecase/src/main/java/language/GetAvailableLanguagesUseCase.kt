package language

interface GetAvailableLanguagesUseCase {
    suspend operator fun invoke(): Result<List<LanguageOption>>
}