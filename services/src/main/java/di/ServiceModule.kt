package di

import locale.LocaleService
import locale.LocaleServiceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val servicesModule = module {
    single<LocaleService> { LocaleServiceImpl(applicationContext = androidContext()) }
}