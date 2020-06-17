package com.bioniex.sextructor.data.exceptions


data class TokenExpired(val messageError: String) : Throwable()

data class CustomError(val messageError: String?) : Throwable()
