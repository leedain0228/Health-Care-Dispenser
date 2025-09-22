package com.example.healthcaredispenser.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth_prefs")

class TokenDataStore(private val context: Context) {
    companion object {
        private val KEY_ACCESS = stringPreferencesKey("access_token")
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { it[KEY_ACCESS] }

    suspend fun save(access: String) {
        context.dataStore.edit { it[KEY_ACCESS] = access }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
