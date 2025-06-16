package language

class LanguageConfigRepositoryImpl(
/* private val localDataSource: LanguageLocalDataSource */
) : LanguageConfigRepository {
    override suspend fun getAvailableLanguages(): Result<List<LanguageOption>> {
        // Option A: Still hardcoded but now in a repository (better than use case)
        val languages = listOf(
            LanguageOption(
                code = "en-US",
                displayName = "English (US)",
                nativeName = "English (US)",
                flagEmoji = "🇺🇸"
            ),
            LanguageOption(
                code = "pt-BR",
                displayName = "Português (Brasil)",
                nativeName = "Português (Brasil)",
                flagEmoji = "🇧🇷"
            )
        )
        return Result.success(languages)

        // Option B: Read from a local data source (e.g., JSON in assets, SharedPreferences for simple cases, XML resource)
        // return localDataSource.fetchAvailableLanguages()
    }
}