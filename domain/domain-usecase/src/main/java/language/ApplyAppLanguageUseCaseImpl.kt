package language

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

class ApplyAppLanguageUseCaseImpl(private val applicationContext: Context) :
    ApplyAppLanguageUseCase {
    override fun invoke(languageCode: String) {
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
}