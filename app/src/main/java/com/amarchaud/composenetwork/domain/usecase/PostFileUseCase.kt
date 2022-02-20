package com.amarchaud.composenetwork.domain.usecase

import arrow.core.Either
import com.amarchaud.composenetwork.domain.models.FolderContentModel
import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError

class PostFileUseCase(
    private val repository: ComposeNetworkRepository
) {
    suspend fun run(
        fileName: String,
        folderId: String,
        data: ByteArray
    ): Either<FolderContentModel, UseCaseError> {
        return repository.postFile(
            fileName = fileName,
            folderId = folderId,
            data = data
        ).map { UseCaseError.handleError(it) }
    }
}