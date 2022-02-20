package com.amarchaud.composenetwork.domain.repository

import arrow.core.Either
import com.amarchaud.composenetwork.data.api.models.ErrorApi
import com.amarchaud.composenetwork.domain.models.FolderContentModel
import com.amarchaud.composenetwork.domain.models.MeModel
import kotlinx.coroutines.flow.Flow

interface ComposeNetworkRepository {
    fun getMasterFolder(): Flow<MeModel?>
    suspend fun disconnect()

    suspend fun getMe(login: String, password: String): Either<MeModel, ErrorApi>
    suspend fun getFolderContent(id: String): Either<List<FolderContentModel>, ErrorApi>
    suspend fun postFile(fileName : String, folderId : String, data : ByteArray) : Either<FolderContentModel, ErrorApi>
    suspend fun postFolder(folderName : String, folderId : String) : Either<FolderContentModel, ErrorApi>
    suspend fun deleteById(id: String): Either<Unit, ErrorApi>
    suspend fun downloadById(id: String): Either<ByteArray, ErrorApi>
}