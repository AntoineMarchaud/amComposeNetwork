package com.amarchaud.composenetwork.data.repository

import arrow.core.Either
import com.amarchaud.composenetwork.data.api.models.ErrorApi
import com.amarchaud.composenetwork.data.exception.NoConnectivityException
import com.amarchaud.composenetwork.data.mappers.toDomain
import com.amarchaud.composenetwork.data.repository.api.ComposeNetworkApiRepository
import com.amarchaud.composenetwork.data.repository.db.ComposeNetworkDbRepository
import com.amarchaud.composenetwork.di.DispatcherModule
import com.amarchaud.composenetwork.domain.models.FolderContentModel
import com.amarchaud.composenetwork.domain.models.MeModel
import com.amarchaud.composenetwork.domain.repository.ComposeNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.SocketTimeoutException
import javax.inject.Inject

class ComposeNetworkRepositoryImp @Inject constructor(
    private val dbRepository: ComposeNetworkDbRepository,
    private val apiRepository: ComposeNetworkApiRepository,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ComposeNetworkRepository {
    override fun getMasterFolder() =
        dbRepository.getMasterFolder()
            .map { if (it?.parentId == null) null else it.toDomain() }.flowOn(ioDispatcher)

    override suspend fun disconnect() {
        dbRepository.deleteUser()
    }

    override suspend fun getMe(login: String, password: String): Either<MeModel, ErrorApi> =
        withContext(ioDispatcher) {
            try {

                dbRepository.deleteUser()
                dbRepository.insertUser(login = login, password = password)

                apiRepository.getMyself().fold(
                    {
                        dbRepository.updateUser(it.toDomain())
                        Either.Left(it.toDomain())
                    },
                    {
                        dbRepository.deleteUser()
                        Either.Right(it)
                    }
                )
            } catch (e: Exception) {
                dbRepository.deleteUser()
                Either.Right(handleException(e))
            }
        }

    override suspend fun getFolderContent(id: String): Either<List<FolderContentModel>, ErrorApi> =
        withContext(ioDispatcher) {
            try {
                apiRepository.getFolderContent(id).mapLeft { it.map { it.toDomain() } }
            } catch (e: Exception) {
                Either.Right(handleException(e))
            }
        }

    override suspend fun postFile(
        fileName: String,
        folderId: String,
        data: ByteArray
    ): Either<FolderContentModel, ErrorApi> = withContext(ioDispatcher) {
        try {
            apiRepository.postFile(
                fileName = fileName,
                folderId = folderId,
                data = data
            ).mapLeft { it.toDomain() }
        } catch (e: Exception) {
            Either.Right(handleException(e))
        }
    }

    override suspend fun postFolder(
        folderName: String,
        folderId: String
    ): Either<FolderContentModel, ErrorApi> = withContext(ioDispatcher) {
        try {
            apiRepository.postFolder(
                folderName = folderName,
                folderId = folderId,
            ).mapLeft { it.toDomain() }
        } catch (e: Exception) {
            Either.Right(handleException(e))
        }
    }

    override suspend fun deleteById(id: String): Either<Unit, ErrorApi> =
        withContext(ioDispatcher) {
            try {
                apiRepository.deleteById(id)
            } catch (e: Exception) {
                Either.Right(handleException(e))
            }
        }

    override suspend fun downloadById(id: String): Either<ByteArray, ErrorApi> =
        withContext(ioDispatcher) {
            try {
                apiRepository.downloadById(id).mapLeft { it.bytes() }
            } catch (e: Exception) {
                Either.Right(handleException(e))
            }
        }


    /**
     * Method for catching generic exception not raised by Retrofit
     */
    private fun handleException(e: Exception): ErrorApi = when (e) {
        is SocketTimeoutException -> ErrorApi.SocketTimeOutError
        is NoConnectivityException -> ErrorApi.NoInternetError
        else -> ErrorApi.ApiGenericServerError
    }
}