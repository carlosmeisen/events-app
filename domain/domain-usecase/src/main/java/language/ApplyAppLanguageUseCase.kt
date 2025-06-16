package language

interface ApplyAppLanguageUseCase {
    operator fun invoke(languageCode: String)
}