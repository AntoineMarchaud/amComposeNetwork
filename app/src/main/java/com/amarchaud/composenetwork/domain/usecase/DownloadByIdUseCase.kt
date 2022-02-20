package com.amarchaud.composenetwork.domain.usecase

import arrow.core.Either
import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError

class DownloadByIdUseCase(
    private val repository: ComposeNetworkRepository
) {
    suspend fun run(id: String): Either<ByteArray, UseCaseError> {
        return repository.downloadById(id).map { UseCaseError.handleError(it) }
    }
}