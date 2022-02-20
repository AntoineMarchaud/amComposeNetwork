package com.amarchaud.composenetwork.domain.usecase

import arrow.core.Either
import com.amarchaud.composenetwork.domain.models.MeModel
import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError

class ConnectUseCase(
    private val repository: ComposeNetworkRepository
) {
    suspend fun run(login: String, password: String): Either<MeModel, UseCaseError> {
        return repository.getMe(login, password).map { UseCaseError.handleError(it) }
    }
}