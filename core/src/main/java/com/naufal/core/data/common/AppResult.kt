package com.naufal.core.data.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

sealed class AppResult<T> {

    data class OnSuccess<T>(val data: T?, val code: Int? = null) : AppResult<T>()

    data class OnFailure<T>(
        val data: T? = null,
        val code: Int? = null,
        val message: String? = null
    ) : AppResult<T>()

    data class OnError<T>(val throwable: Throwable?) : AppResult<T>()

}

inline fun <reified T> AppResult<T>.addOnResultListener(
    onSuccess: (data: T?) -> Unit,
    onFailure: (data: T?, code: Int?, message: String?) -> Unit,
    onError: (throwable: Throwable?) -> Unit
) {

    when (this) {
        is AppResult.OnSuccess -> onSuccess(this.data)
        is AppResult.OnFailure -> onFailure(this.data, this.code, this.message)
        is AppResult.OnError -> onError(this.throwable)
    }

}

inline fun <reified T> errorBody(json: String?): T =
    Gson().fromJson(json, object : TypeToken<T>() {}.type)