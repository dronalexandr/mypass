package com.vegasoft.mypasswords.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class BaseDBHelper extends SQLiteOpenHelper {

    // Table names
    static final String TABLE_RECORD = "records_table";

    //Record table
    static final String RECORD_ID = "id";
    static final String RECORD_NAME = "name";
    static final String RECORD_DATE = "date";
    static final String RECORD_GROUP = "group_name";
    static final String RECORD_SITE = "site_url";
    static final String RECORD_USER = "user";
    static final String RECORD_PASS = "pass";
    static final String RECORD_IMAGE = "image_path";
    static final String RECORD_ENCRYPTION = "encryption";

    // All Static variables
    private static final String TAG = BaseDBHelper.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PreferenceCentral.db";

    BaseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createRecordsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropRecordsTable(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void createRecordsTable(SQLiteDatabase db) {
        if (db == null)
            db = getWritableDatabase();
        db.execSQL("CREATE TABLE " + TABLE_RECORD + "("
                + RECORD_ID + " LONG PRIMARY KEY, "
                + RECORD_NAME + " TEXT, "
                + RECORD_DATE + " LONG, "
                + RECORD_GROUP + " TEXT, "
                + RECORD_SITE + " TEXT, "
                + RECORD_USER + " TEXT, "
                + RECORD_PASS + " TEXT, "
                + RECORD_IMAGE + " TEXT, "
                + RECORD_ENCRYPTION + " INTEGER ) " );
        Log.d(TAG, "SQLiteDatabase onCreate(SQLiteDatabase db) "
                + TABLE_RECORD);
    }

    private void dropRecordsTable(SQLiteDatabase db) {
        if (db == null)
            db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
    }
}
