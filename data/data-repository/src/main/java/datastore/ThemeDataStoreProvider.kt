package datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

 val Context.provideThemeDataStore by preferencesDataStore(name = "theme_preferences")