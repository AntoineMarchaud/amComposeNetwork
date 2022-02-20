package com.amarchaud.composenetwork.data.repository.api

import arrow.core.Either
import com.amarchaud.composenetwork.data.api.ComposeNetworkApi
import com.amarchaud.composenetwork.data.api.CreateFolderCommand
import com.amarchaud.composenetwork.data.api.models.ErrorApi
import com.amarchaud.composenetwork.data.api.models.FolderContentApiModel
import com.amarchaud.composenetwork.data.api.models.MeApiModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


class ComposeNetworkApiRepositoryImp @Inject constructor(private val api: ComposeNetworkApi) : ComposeNetworkApiRepository {

    override suspend fun getMyself(): Either<MeApiModel, ErrorApi> {
        return apiResult(response = api.getMyself())
    }

    override suspend fun getFolderContent(id: String): Either<List<FolderContentApiModel>, ErrorApi> {
        return apiResult(response = api.getFolderContent(id))
    }

    override suspend fun postFile(
        fileName: String,
        folderId: String,
        data: ByteArray
    ): Either<FolderContentApiModel, ErrorApi> {
        return apiResult(
            response = api.postFile(
                name = "attachment;filename*=utf-8''$fileName",
                folderId = folderId,
                rawFile = data.toRequestBody(
                    "application/octet-stream".toMediaTypeOrNull(),
                    0,
                    data.size
                )
            )
        )
    }

    override suspend fun postFolder(
        folderName: String,
        folderId: String
    ): Either<FolderContentApiModel, ErrorApi> {
        return apiResult(
            response = api.postFolder(
                folderId = folderId,
                folderName = CreateFolderCommand(
                    name = folderName
                )
            )
        )
    }

    override suspend fun deleteById(id: String): Either<Unit, ErrorApi> {
        return apiResult(response = api.deleteById(id))
    }

    override suspend fun downloadById(id: String): Either<ResponseBody, ErrorApi> {
        return apiResult(response = api.downloadById(id))
    }

    private inline fun <reified T> apiResult(response: Response<T>): Either<T, ErrorApi> {

        return try {
            when (response.isSuccessful) {
                true -> {
                    return if ((response.code() == 200 || response.code() == 201) && response.body() != null) {
                        Either.Left(response.body()!!)
                    } else if (response.code() == 204) {
                        when (T::class) {
                            Unit::class -> Either.Left(Unit as T)
                            Boolean::class -> Either.Left(true as T)
                            else -> Either.Right(ErrorApi.ApiNullBody)
                        }
                    } else {
                        Either.Right(ErrorApi.ApiNullBody)
                    }
                }
                false -> {
                    Either.Right(ErrorApi.ApiServerErrorWithCode(response.code(), response.message()))
                }
            }
        } catch (e: Throwable) {
            Either.Right(ErrorApi.ApiGenericServerError)
        }
    }
}