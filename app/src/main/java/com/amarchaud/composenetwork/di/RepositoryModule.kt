package com.amarchaud.composenetwork.di

import com.amarchaud.composenetwork.data.repository.ComposeNetworkRepositoryImp
import com.amarchaud.composenetwork.data.repository.api.ComposeNetworkApiRepository
import com.amarchaud.composenetwork.data.repository.api.ComposeNetworkApiRepositoryImp
import com.amarchaud.composenetwork.data.repository.db.ComposeNetworkDbRepository
import com.amarchaud.composenetwork.data.repository.db.ComposeNetworkDbRepositoryImp
import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindApiRepo(appRepository: ComposeNetworkApiRepositoryImp): ComposeNetworkApiRepository

    @Singleton
    @Binds
    abstract fun bindDbRepo(appRepository: ComposeNetworkDbRepositoryImp): ComposeNetworkDbRepository

    @Singleton
    @Binds
    abstract fun bindRepo(appRepository: ComposeNetworkRepositoryImp): ComposeNetworkRepository
}