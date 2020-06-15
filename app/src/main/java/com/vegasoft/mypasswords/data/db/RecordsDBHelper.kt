package com.vegasoft.mypasswords.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.vegasoft.mypasswords.data.entity.Record
import java.util.*

class RecordsDBHelper(context: Context?) : BaseDBHelper(context) {
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    fun deleteRecord(id: Long) {
        val db = writableDatabase
        val delete = db.delete(TABLE_RECORD, "$RECORD_ID=?", arrayOf(id.toString()))
        Log.d(TAG, "deleteNotificationByPreference: deleted $delete rows!")
        db.close()
    }

    fun putRecord(record: Record) {
        val db = writableDatabase
        if (record.id != null) {
            updateRecord(db, record)
        } else {
            record.id = System.currentTimeMillis()
            addRecord(db, record)
        }
    }

    fun putRecord(record: Record, isRestored: Boolean) {
        if (isRestored) {
            addRecord(writableDatabase, record)
        } else {
            putRecord(record)
        }
    }

    private fun addRecord(db: SQLiteDatabase, record: Record) {
        val values = ContentValues()
        values.put(RECORD_ID, record.id)
        values.put(RECORD_NAME, record.name)
        values.put(RECORD_DATE, record.date)
        values.put(RECORD_GROUP, record.group)
        values.put(RECORD_SITE, record.site)
        values.put(RECORD_USER, record.user)
        values.put(RECORD_PASS, record.pass)
        values.put(RECORD_IMAGE, record.image)
        db.insert(TABLE_RECORD, null, values)
        db.close()
    }

    private fun updateRecord(db: SQLiteDatabase, record: Record) {
        val values = ContentValues()
        values.put(RECORD_NAME, record.name)
        values.put(RECORD_DATE, record.date)
        values.put(RECORD_GROUP, record.group)
        values.put(RECORD_SITE, record.site)
        values.put(RECORD_USER, record.user)
        values.put(RECORD_PASS, record.pass)
        values.put(RECORD_IMAGE, record.image)
        db.update(TABLE_RECORD, values, "$RECORD_ID = ?", arrayOf(record.id.toString()))
        db.close()
    }

    private fun recordExist(db: SQLiteDatabase, id: Long): Boolean {
        val query = "Select * from $TABLE_RECORD where $RECORD_ID = $id ;"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    val records: ArrayList<Record>
        get() {
            val records = ArrayList<Record>()
            val db = this.writableDatabase
            var cursor: Cursor? = null
            try {
                cursor = db.query(TABLE_RECORD, arrayOf(RECORD_ID, RECORD_NAME,
                        RECORD_DATE, RECORD_GROUP, RECORD_SITE,
                        RECORD_USER, RECORD_PASS, RECORD_IMAGE), null,
                        null, null, null, RECORD_NAME, null)
                if (cursor.moveToFirst()) {
                    do {
                        val record = Record()
                        record.id = cursor.getLong(0)
                        record.name = cursor.getString(1)
                        record.date = cursor.getLong(2)
                        record.group = cursor.getString(3)
                        record.site = cursor.getString(4)
                        record.user = cursor.getString(5)
                        record.pass = cursor.getString(6)
                        record.image = cursor.getString(7)
                        records.add(record)
                    } while (cursor.moveToNext())
                }
            } catch (e: Exception) {
                Log.e(TAG, "getRecords(): ", e)
            } finally {
                cursor?.close()
                db.close()
            }
            return records
        }

    companion object {
        private val TAG = RecordsDBHelper::class.java.simpleName
    }
}