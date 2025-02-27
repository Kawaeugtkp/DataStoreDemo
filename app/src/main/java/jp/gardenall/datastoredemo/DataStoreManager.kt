package jp.gardenall.datastoredemo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DataStoreManager(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "TOKEN_KEY")
    private val dataStore = context.dataStore

    companion object {
        val darkModeKey = booleanPreferencesKey("DARK_MODE_KEY")
    }

    suspend fun setTheme(isDarkMode: Boolean) {
        dataStore.edit { pref ->
            pref[darkModeKey] = isDarkMode
        }
    }

    fun getTheme(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {  pref ->
                val uiMode = pref[darkModeKey] ?: false
                uiMode
            }
    }
}