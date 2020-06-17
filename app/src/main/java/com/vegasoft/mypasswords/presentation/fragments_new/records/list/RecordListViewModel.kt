package com.vegasoft.mypasswords.presentation.fragments_new.records.list

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.vegasoft.mypasswords.data.persistence.SecureDatabase
import com.vegasoft.mypasswords.data.persistence.models.Record
import com.vegasoft.mypasswords.presentation.BaseViewModel
import com.vegasoft.mypasswords.presentation.ui_models.SecuredData
import com.vegasoft.mypasswords.presentation.ui_models.UIRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecordListViewModel : BaseViewModel() {
    val _records = MutableLiveData<ArrayList<UIRecord>>()

    fun loadRecords(context: Context) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            val records = SecureDatabase.getInstance(context).recordsDao().getRecords()
            val uiRecords = arrayListOf<UIRecord>()
            records.forEach { record ->
                uiRecords.add(UIRecord(
                        record.id,
                        record.title,
                        record.data.let { Gson().fromJson(it, SecuredData::class.java) }?: SecuredData(),
                        record.image,
                        record.userId
                ))
            }
            withContext(Dispatchers.Main) {
                _records.postValue(uiRecords)
            }
        }
    }

    fun removeRecord(context: Context, id: String) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            SecureDatabase.getInstance(context).recordsDao().deleteRecordById(id)
        }
    }

    fun addRecord(context: Context, uiRecord: UIRecord) {
        mScope.launch(Dispatchers.IO + gerErrorHandler()) {
            SecureDatabase.getInstance(context).recordsDao().insertRecord(Record(
                    uiRecord.id,
                    uiRecord.userId,
                    Gson().toJson(uiRecord.securedData),
                    uiRecord.title,
                    uiRecord.image
            ))
        }
    }
}
