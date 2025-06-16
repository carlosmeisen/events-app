package language

interface LanguageConfigRepository {
    // ... other language-related methods like get/save preferences
    suspend fun getAvailableLanguages(): Result<List<LanguageOption>> // Or Flow for caching strategies
}