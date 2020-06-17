package com.bioniex.sextructor.data.exceptions

import com.vegasoft.mypasswords.data.exceptions.ErrorMessageCode

data class ExceptionModel(
        val message: String,
        val errorMessageCode: ErrorMessageCode = ErrorMessageCode.UNKNOWN,
        val emergencyBreakSession: Boolean = false
)
