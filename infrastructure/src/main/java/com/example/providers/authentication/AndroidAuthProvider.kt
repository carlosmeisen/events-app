package com.example.providers.authentication

import android.content.Context
import android.content.SharedPreferences
import providers.AuthProvider

class AndroidAuthProvider(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : AuthProvider {

    override fun isAuthenticated(): Boolean {
        // Check actual authentication state
        return sharedPreferences.getBoolean("is_authenticated", false)
    }
}