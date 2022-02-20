package com.amarchaud.composenetwork.data.api.models

sealed class ErrorApi {
    class ApiServerErrorWithCode(val code: Int, val message: String) : ErrorApi()
    object ApiNullBody : ErrorApi()
    object ApiGenericServerError : ErrorApi()
    object SocketTimeOutError : ErrorApi()
    object NoInternetError : ErrorApi()
}