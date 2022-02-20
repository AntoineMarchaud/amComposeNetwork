package com.amarchaud.composenetwork.domain.usecase

import arrow.core.Either
import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError

class DeleteByIdUseCase(
    private val repository: ComposeNetworkRepository
) {
    suspend fun run(id: String): Either<Unit, UseCaseError> {
        return repository.deleteById(id).map { UseCaseError.handleError(it) }
    }
}