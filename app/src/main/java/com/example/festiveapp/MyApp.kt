package com.example.festiveapp

import android.app.Application
import com.example.di.infrastructureModule
import com.example.festiveapp.di.appDataStoreModule
import com.example.festiveapp.di.appModule
import di.dataModule
import com.example.feature_login.di.featureLoginModule
import di.domainUseCaseModule // Updated import
import di.featureHomeModule
import di.featureSettingsModule
import di.loggingModule
import di.navigationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ui.theme.ThemeApplier

class MyApp : Application() {

    private val themeApplier: ThemeApplier by inject()

    // Create a structured CoroutineScope for application-wide operations.
    // This is better than GlobalScope for managing coroutine lifecycles
    // and ensuring cancellation when the application process dies.
    private val applicationScope = CoroutineScope(context = Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        // Initialize Koin only if it hasn't been started already.
        // This check prevents re-initialization issues, especially if Koin is
        // also initialized in another component (like MainActivity, though less common now).
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidLogger(level = Level.DEBUG) // Or Level.DEBUG for more verbose logs in development
                androidContext(androidContext = this@MyApp) // Provide application context to Koin
                modules(
                    appModule,
                    appDataStoreModule,      // Includes appModule and appDataStoreModule
                    dataModule,         // Now properly imports and includes the data layer's Koin module
                    navigationModule,   // Ensures navigation components are available
                    infrastructureModule, // Ensures platform-specific implementations are available
                    loggingModule,
                    domainUseCaseModule,
                    featureSettingsModule,
                    featureHomeModule,
                    featureLoginModule,
                    // Add other Koin modules from your other feature/domain modules here as your app grows
                )
            }
        }

        themeApplier.startApplying(scope = applicationScope)
    }


}