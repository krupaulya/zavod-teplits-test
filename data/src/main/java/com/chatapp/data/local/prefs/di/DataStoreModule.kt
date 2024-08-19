package com.chatapp.data.local.prefs.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.chatapp.data.local.prefs.JwtTokenDataStore
import com.chatapp.data.local.prefs.JwtTokenManager
import com.chatapp.data.local.prefs.UserDataStore
import com.chatapp.data.local.prefs.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

private const val USER_ID_PREFERENCES = "user_id_preferences"
private const val AUTH_PREFERENCES = "auth_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    @Named("TokenPreferences")
    fun provideTokenDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { appContext.preferencesDataStoreFile(USER_ID_PREFERENCES) }
        )
    }

    @Provides
    @Singleton
    @Named("UserPreferences")
    fun provideUserDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { appContext.preferencesDataStoreFile(AUTH_PREFERENCES) }
        )
    }

    @Provides
    @Singleton
    fun provideJwtTokenManager(@Named("TokenPreferences") dataStore: DataStore<Preferences>): JwtTokenManager {
        return JwtTokenDataStore(dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun provideUserManager(@Named("UserPreferences") dataStore: DataStore<Preferences>): UserManager {
        return UserDataStore(dataStore = dataStore)
    }
}