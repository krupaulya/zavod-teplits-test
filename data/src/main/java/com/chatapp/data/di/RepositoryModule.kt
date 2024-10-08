package com.chatapp.data.di

import com.chatapp.data.RepositoryImpl
import com.chatapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository
}