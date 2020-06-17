package com.bioniex.sextructor.data.exceptions

import com.vegasoft.mypasswords.data.exceptions.ErrorMessageCode
import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.SocketTimeoutException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

@Suppress("FunctionName")
inline fun simpleExceptionHandler(crossinline handler: (error: ExceptionModel) -> Unit): CoroutineExceptionHandler =
    object : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

        override fun handleException(context: CoroutineContext, exception: Throwable) =
            handler.invoke(processError(exception))
    }

fun processError(exception: Throwable?): ExceptionModel {
    return when (exception) {
        is IllegalStateException -> ExceptionModel(
            exception.message(),
            ErrorMessageCode.ILLEGAL_STATE_EXCEPTION
        )
        is SocketTimeoutException -> ExceptionModel(
            exception.message(),
            ErrorMessageCode.SOCKET_TIMEOUT_EXCEPTION
        )
        is KotlinNullPointerException -> ExceptionModel(
            exception.message(),
            ErrorMessageCode.KOTLIN_NULL_POINTER_EXCEPTION
        )
        is TokenExpired -> {
            ExceptionModel(exception.message(), ErrorMessageCode.TOKEN_EXPIRED, true)
        }
        else -> ExceptionModel(exception.message(), ErrorMessageCode.UNKNOWN)
    }
}

fun Throwable?.message(): String {
    return this?.localizedMessage ?: this?.message.ifNullShowUnknownError()
}

fun String?.ifNullShowErrorMessage(): String {
    return this ?: "Success but a message from server null"
}

fun String?.ifNullShowUnknownError(): String {
    return this ?: "Unknown error"
}