package com.example.showmagnet.utils

import com.example.showmagnet.domain.model.common.NetworkUnavailableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


fun <T> repositoryFlow(
    localFlow: Flow<T>, updateFun: suspend () -> Unit
): Flow<Result<T>> = channelFlow {
    launch {
        localFlow.collect {
            send(Result.success(it))
        }
    }

    launch {
        updateFun()
    }
}.handleErrors()

fun <T> Flow<Result<T>>.handleErrors(): Flow<Result<T>> = flow {
    try {
        collect { value -> emit(value) }
    } catch (e: Throwable) {
        e.printStackTrace()
        emit(Result.failure(e))
    }
}


fun <T> Flow<Result<T>>.collectResult(
    scope: CoroutineScope,
    onSuccess: (T) -> Unit,
    onFailure: (String) -> Unit,
    onNetworkFailure: (String) -> Unit
) {
    scope.launch {
        collect {
            it.handle(onSuccess, onFailure, onNetworkFailure)
        }
    }
}

fun <T> Result<T>.handle(
    onSuccess: (T) -> Unit, onFailure: (String) -> Unit, onNetworkFailure: (String) -> Unit
) {
    if (isFailure) {
        if (isNetworkFailure()) {
            onNetworkFailure(exceptionOrNull()?.localizedMessage.orEmpty())
        } else {
            onFailure(exceptionOrNull()?.localizedMessage.orEmpty())
        }
    } else {
        onSuccess(getOrThrow())
    }
}

fun <T> Result<T>.isNetworkFailure(): Boolean =
    this.exceptionOrNull() is NetworkUnavailableException
