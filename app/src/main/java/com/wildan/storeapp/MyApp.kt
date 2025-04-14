package com.wildan.storeapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MyApp : Application() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
    private val Context.authRemember: DataStore<Preferences> by preferencesDataStore(name = "authRemember")

    suspend fun saveStringDataStore(context: Context, key: String, value: String?) =
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it[stringPreferencesKey(key)] = value.toString()
            }
        }

    suspend fun saveAuthDataStore(context: Context, key: String, value: String?) =
        withContext(Dispatchers.IO) {
            context.authRemember.edit {
                it[stringPreferencesKey(key)] = value.toString()
            }
        }

    suspend fun saveRememberDataStore(context: Context, key: String, value: Boolean) =
        withContext(Dispatchers.IO) {
            context.authRemember.edit {
                it[booleanPreferencesKey(key)] = value
            }
        }

    suspend fun clearDataStore(context: Context) =
        withContext(Dispatchers.IO) {
            context.dataStore.edit {
                it.clear()
            }
        }

    suspend fun clearAuthStore(context: Context) =
        withContext(Dispatchers.IO) {
            context.authRemember.edit {
                it.clear()
            }
        }


    suspend fun readStringDataStore(context: Context, key: String): String = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[stringPreferencesKey(key)] ?: "-" }
        .flowOn(Dispatchers.IO)
        .first()

    suspend fun readAuthDataStore(context: Context, key: String): String = context.authRemember.data
        .catch { emit(emptyPreferences()) }
        .map { it[stringPreferencesKey(key)] ?: "-" }
        .flowOn(Dispatchers.IO)
        .first()

    suspend fun readRememberDataStore(context: Context, key: String): Boolean =
        context.authRemember.data
            .catch { emit(emptyPreferences()) }
            .map { it[booleanPreferencesKey(key)] ?: false }
            .flowOn(Dispatchers.IO)
            .first()

    companion object {
        private var instance: MyApp? = null

        @Synchronized
        fun getInstance(): MyApp {
            if (instance == null) {
                instance = MyApp()
            }
            return instance as MyApp
        }
    }
}