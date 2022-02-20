package com.amarchaud.composenetwork.domain.usecase

import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository

class DisconnectUseCase(
    private val repository: ComposeNetworkRepository
) {
    suspend fun run() {
        repository.disconnect()
    }
}