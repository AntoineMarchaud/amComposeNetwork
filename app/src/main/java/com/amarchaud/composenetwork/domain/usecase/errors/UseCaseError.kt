package com.amarchaud.composenetwork.domain.usecase.errors

import com.amarchaud.composenetwork.data.api.models.ErrorApi

sealed class UseCaseError {
    object BadRequest : UseCaseError() // 400
    object ApiNotAuthorize : UseCaseError() // incorrect login (401)
    object ItemNotFound : UseCaseError() // 404
    object InternalError : UseCaseError() // 500
    object GenericUseCaseError : UseCaseError()
    object NoInternetError : UseCaseError()
    object SocketTimeOut : UseCaseError()

    companion object {
        internal fun handleError(error: ErrorApi) = when (error) {
            is ErrorApi.ApiServerErrorWithCode -> {
                when (error.code) {
                    400 -> BadRequest
                    401 -> ApiNotAuthorize
                    404 -> ItemNotFound
                    500 -> InternalError
                    else -> GenericUseCaseError
                }
            }
            is ErrorApi.NoInternetError -> NoInternetError
            is ErrorApi.SocketTimeOutError -> SocketTimeOut
            else -> GenericUseCaseError
        }
    }
}