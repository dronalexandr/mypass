package com.vegasoft.mypasswords.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

open class BaseDBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        createRecordsTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropRecordsTable(db)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    private fun createRecordsTable(_db: SQLiteDatabase) {
        val db: SQLiteDatabase? = if (_db == null) writableDatabase else _db
        db!!.execSQL("CREATE TABLE " + TABLE_RECORD + "("
                + RECORD_ID + " LONG PRIMARY KEY, "
                + RECORD_NAME + " TEXT, "
                + RECORD_DATE + " LONG, "
                + RECORD_GROUP + " TEXT, "
                + RECORD_SITE + " TEXT, "
                + RECORD_USER + " TEXT, "
                + RECORD_PASS + " TEXT, "
                + RECORD_IMAGE + " TEXT, "
                + RECORD_ENCRYPTION + " INTEGER ) ")
        Log.d(TAG, "SQLiteDatabase onCreate(SQLiteDatabase db) "
                + TABLE_RECORD)
    }

    private fun dropRecordsTable(_db: SQLiteDatabase) {
        val db: SQLiteDatabase? = if (_db == null) writableDatabase else _db
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RECORD")
    }

    companion object {
        // Table names
        const val TABLE_RECORD = "records_table"

        //UIRecord table
        const val RECORD_ID = "id"
        const val RECORD_NAME = "name"
        const val RECORD_DATE = "date"
        const val RECORD_GROUP = "group_name"
        const val RECORD_SITE = "site_url"
        const val RECORD_USER = "user"
        const val RECORD_PASS = "pass"
        const val RECORD_IMAGE = "image_path"
        const val RECORD_ENCRYPTION = "encryption"

        // All Static variables
        private val TAG = BaseDBHelper::class.java.simpleName

        // Database Version
        private const val DATABASE_VERSION = 2

        // Database Name
        private const val DATABASE_NAME = "PSM.db"
    }
}