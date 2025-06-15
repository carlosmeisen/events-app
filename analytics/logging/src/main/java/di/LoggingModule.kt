package di

import AndroidLogWriter
import DefaultGenericLogger
import DefaultNavigationLogger
import GenericLogger
import LogWriter
import NavigationLogger
import org.koin.dsl.module

val loggingModule = module {
    // Core logging
    single<LogWriter> { AndroidLogWriter() }

    // Generic logging
    single<GenericLogger> { DefaultGenericLogger(get()) }

    // Domain-specific logging
    single<NavigationLogger> { DefaultNavigationLogger(get()) }
}