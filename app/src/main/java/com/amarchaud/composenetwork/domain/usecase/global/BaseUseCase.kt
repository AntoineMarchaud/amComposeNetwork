package com.amarchaud.composenetwork.domain.usecase.global

import arrow.core.Either
import com.amarchaud.composenetwork.data.api.models.ErrorApi
import com.amarchaud.composenetwork.domain.usecase.errors.UseCaseError

interface BaseUseCase<out Type, in Params> where Type : Any {
    suspend fun run(params: Params) : Either<Type, UseCaseError>
    fun handleError(error : ErrorApi) : UseCaseError
}