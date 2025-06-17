package com.example.festiveapp.di

import com.example.festiveapp.BuildConfig
import com.example.festiveapp.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin module for application-level dependencies, including navigation and settings ViewModels.
 */
val appModule: Module = module {
    single(named("IsAppDebug")) { BuildConfig.DEBUG }

    viewModel {
        SplashViewModel(
            initializeAppUseCase = get(),
            localeService = get()
        )
    }
}
