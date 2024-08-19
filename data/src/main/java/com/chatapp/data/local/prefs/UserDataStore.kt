package com.chatapp.data.local.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStore @Inject constructor(private val dataStore: DataStore<Preferences>): UserManager {

    companion object {
        val USER_ID_KEY = intPreferencesKey("user_id")
    }

    override suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    override suspend fun getUserId(): Int? {
        return dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }.first()
    }
}