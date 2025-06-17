package locale

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

class LocaleServiceImpl(
    private val applicationContext: Context
) : LocaleService {
    override fun updateLocale(languageCode: String) {
        if (languageCode.isEmpty()) return

        val parts = languageCode.split("-")
        val language = parts[0]
        val country = parts.getOrElse(1) { "" }.uppercase(Locale.ROOT)
        val locale = Locale(language, country)

        Locale.setDefault(locale)
        val config = Configuration(applicationContext.resources.configuration)
        config.setLocale(locale)
        applicationContext.resources.updateConfiguration(
            config,
            applicationContext.resources.displayMetrics
        )
    }

    override fun getCurrentLocale(): String {
        val locale = applicationContext.resources.configuration.locales[0]
        return "${locale.language}-${locale.country}"
    }
}