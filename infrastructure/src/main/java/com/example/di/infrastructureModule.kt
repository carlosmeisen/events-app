package com.example.di

import android.content.Context
import android.content.SharedPreferences
import com.example.external.AndroidExternalAppLauncher
import com.example.providers.authentication.AndroidAuthProvider
import com.example.providers.network.AndroidNetworkProvider
import com.example.theme.AndroidThemeApplier
import external.ExternalAppLauncher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import providers.AuthProvider
import providers.NetworkProvider
import ui.theme.ThemeApplier

private const val MY_APP_PREFERENCES = "portuguese_events_app"

val infrastructureModule = module {
    // Provides the Android-specific implementation for launching external apps.
    single<ExternalAppLauncher> { AndroidExternalAppLauncher(context = get()) } // 'get()' will resolve Context

    // NEW: Provides the Android-specific implementation for applying themes.
    // It depends on ObserveAndApplyThemeUseCase which is provided by domain:domain-usecase.
    single<ThemeApplier> { AndroidThemeApplier(getThemePreferenceUseCase = get()) }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(MY_APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<AuthProvider> {
        AndroidAuthProvider(
            context = androidContext(),
            sharedPreferences = get() // Assuming you have SharedPreferences in DI
        )
    }

    single<NetworkProvider> {
        AndroidNetworkProvider(androidContext())
    }
}