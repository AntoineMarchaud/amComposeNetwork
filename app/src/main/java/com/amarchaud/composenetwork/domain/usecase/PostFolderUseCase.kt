package com.amarchaud.composenetwork.domain.usecase

import arrow.core.Either
import com.amarchaud.composenetwork.domain.models.FolderContentModel
import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError

class PostFolderUseCase(
    private val repository: ComposeNetworkRepository
) {
    suspend fun run(
        folderName: String,
        folderId: String
    ): Either<FolderContentModel, UseCaseError> {
        return repository.postFolder(
            folderName = folderName,
            folderId = folderId
        ).map { UseCaseError.handleError(it) }
    }
}