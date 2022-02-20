package com.amarchaud.composenetwork.domain.usecase

import arrow.core.Either
import com.amarchaud.composenetwork.domain.models.FolderContentModel
import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError

class GetFolderContentUseCase(private val repository: ComposeNetworkRepository) {

    suspend fun run(id: String): Either<List<FolderContentModel>, UseCaseError> {
        return repository.getFolderContent(id).map { UseCaseError.handleError(it) }
    }
}