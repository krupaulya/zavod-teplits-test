package com.chatapp.data.local.database.di

import android.content.Context
import androidx.room.Room
import com.chatapp.data.local.database.ChatsDatabase
import com.chatapp.data.local.database.dao.ChatsDao
import com.chatapp.data.local.database.dao.CountryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ChatsDatabase {
        return Room.databaseBuilder(
            appContext,
            ChatsDatabase::class.java,
            "countries.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCountryDao(database: ChatsDatabase): CountryDao {
        return database.countryDao()
    }

    @Provides
    fun provideChatsDao(database: ChatsDatabase): ChatsDao {
        return database.chatsDao()
    }
}