package com.amarchaud.composenetwork.di

import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import com.amarchaud.composenetwork.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun provideConnectUseCase(
        composeNetworkRepository: ComposeNetworkRepository
    ): ConnectUseCase =
        ConnectUseCase(
            repository = composeNetworkRepository
        )

    @Provides
    fun provideIsConnectedUseCase(
        composeNetworkRepository: ComposeNetworkRepository
    ): GetMasterFolderUseCase =
        GetMasterFolderUseCase(
            repository = composeNetworkRepository
        )

    @Provides
    fun provideGetFolderContentUseCase(
        composeNetworkRepository: ComposeNetworkRepository
    ): GetFolderContentUseCase =
        GetFolderContentUseCase(
            repository = composeNetworkRepository
        )

    @Provides
    fun provideDisconnectUseCase(
        composeNetworkRepository: ComposeNetworkRepository
    ): DisconnectUseCase =
        DisconnectUseCase(
            repository = composeNetworkRepository
        )

    @Provides
    fun provideDeleteByIdUseCase(
        composeNetworkRepository: ComposeNetworkRepository
    ): DeleteByIdUseCase =
        DeleteByIdUseCase(
            repository = composeNetworkRepository
        )

    @Provides
    fun provideDownloadByIdUseCase(
        composeNetworkRepository: ComposeNetworkRepository
    ): DownloadByIdUseCase =
        DownloadByIdUseCase(
            repository = composeNetworkRepository
        )

    @Provides
    fun providePostFolderUseCase(
        composeNetworkRepository: ComposeNetworkRepository
    ): PostFolderUseCase =
        PostFolderUseCase(
            repository = composeNetworkRepository
        )

    @Provides
    fun providePostFileUseCase(
        composeNetworkRepository: ComposeNetworkRepository
    ): PostFileUseCase =
        PostFileUseCase(
            repository = composeNetworkRepository
        )
}