package com.chatapp.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

fun <T, R> toResultFlow(
    call: suspend () -> Response<T>,
    mapper: (T) -> R
): Flow<ApiResult<R>> {
    return flow {
        emit(ApiResult.Loading<R>(null, true))
        val c = call()
        c.let { result ->
            try {
                if (c.isSuccessful && c.body() != null) {
                    val mappedResult = mapper(result.body()!!)
                    emit(ApiResult.Success(mappedResult))
                } else {
                    c.errorBody()?.let {
                        emit(ApiResult.Error(it.string()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.toString()))
            }
        }
    }.flowOn(Dispatchers.IO)
}