package locale

interface LocaleService {
    fun updateLocale(languageCode: String)
    fun getCurrentLocale(): String
}