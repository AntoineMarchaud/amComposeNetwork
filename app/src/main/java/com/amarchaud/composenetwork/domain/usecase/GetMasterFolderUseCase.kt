package com.amarchaud.composenetwork.domain.usecase

import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository

class GetMasterFolderUseCase(private val repository: ComposeNetworkRepository) {
    fun run() = repository.getMasterFolder()
}