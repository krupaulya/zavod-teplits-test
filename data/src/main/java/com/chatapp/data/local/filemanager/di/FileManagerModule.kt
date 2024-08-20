package com.chatapp.data.local.filemanager.di

import com.chatapp.data.local.filemanager.FileManager
import com.chatapp.data.local.filemanager.FileManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileManagerModule {

    @Provides
    @Singleton
    fun provideFileManager(): FileManager {
        return FileManagerImpl()
    }
}