package com.amarchaud.composenetwork.data.repository.api

import arrow.core.Either
import com.amarchaud.composenetwork.data.api.models.ErrorApi
import com.amarchaud.composenetwork.data.api.models.FolderContentApiModel
import com.amarchaud.composenetwork.data.api.models.MeApiModel
import okhttp3.ResponseBody

interface ComposeNetworkApiRepository {
    suspend fun getMyself(): Either<MeApiModel, ErrorApi>
    suspend fun getFolderContent(id : String) : Either<List<FolderContentApiModel>, ErrorApi>
    suspend fun postFile(fileName : String, folderId : String, data : ByteArray) : Either<FolderContentApiModel, ErrorApi>
    suspend fun postFolder(folderName : String, folderId : String) : Either<FolderContentApiModel, ErrorApi>
    suspend fun deleteById(id : String) : Either<Unit, ErrorApi>
    suspend fun downloadById(id : String) : Either<ResponseBody, ErrorApi>
}