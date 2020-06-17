package com.vegasoft.mypasswords.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bioniex.sextructor.data.exceptions.ExceptionModel
import com.bioniex.sextructor.data.exceptions.simpleExceptionHandler
import com.vegasoft.mypasswords.utils.getCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel() : ViewModel() {
    val mScope = getCoroutineScope()
    val error = MutableLiveData<ExceptionModel>()
    fun gerErrorHandler() = simpleExceptionHandler { error ->
        mScope.launch {
            withContext(Dispatchers.Main) {
                this@BaseViewModel.error.postValue(error)
            }
        }
    }
}