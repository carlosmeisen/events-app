package language

data class LanguageOption(
    val code: String,        // e.g., "en", "es", "pt-BR" (ISO 639-1 or BCP 47 codes are common)
    val displayName: String, // e.g., "English", "Español", "Português (Brasil)" - for display in the current app language
    val nativeName: String? = null, // e.g., "English", "Español", "Português" - the language's name in its own script/language
    val flagEmoji: String? = null  // e.g., "🇬🇧", "🇪🇸", "🇧🇷" - for visual flair
)