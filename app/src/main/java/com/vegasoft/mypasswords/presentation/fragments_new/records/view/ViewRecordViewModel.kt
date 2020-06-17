package com.vegasoft.mypasswords.presentation.fragments_new.records.view

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.vegasoft.mypasswords.data.persistence.SecureDatabase
import com.vegasoft.mypasswords.data.persistence.models.Record
import com.vegasoft.mypasswords.presentation.BaseViewModel
import com.vegasoft.mypasswords.presentation.ui_models.UIRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ViewRecordViewModel : BaseViewModel() {

    fun updateRecord(context: Context, uiRecord: UIRecord?) {
        val recordsDao = SecureDatabase.getInstance(context).recordsDao()
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val id = uiRecord?.id ?: UUID.randomUUID().toString()
            recordsDao.insertRecord(Record(
                    if (TextUtils.isEmpty(id)) UUID.randomUUID().toString() else id,
                    uiRecord?.userId ?: "",
                    uiRecord?.securedData?.let { Gson().toJson(it) } ?: "",
                    uiRecord?.title ?: "",
                    uiRecord?.image ?: ""
            ))
        }
    }

    fun removeRecord(context: Context, id: String) {
        val recordsDao = SecureDatabase.getInstance(context).recordsDao()
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            recordsDao.deleteRecordById(id)
        }
    }
}
