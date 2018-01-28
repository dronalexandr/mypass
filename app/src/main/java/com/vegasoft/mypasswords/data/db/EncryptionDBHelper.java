package com.vegasoft.mypasswords.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vegasoft.mypasswords.data.entity.Record;

import java.util.ArrayList;


public class EncryptionDBHelper extends BaseDBHelper {

    private static final String TAG = EncryptionDBHelper.class.getSimpleName();

    public EncryptionDBHelper(Context context) {
        super(context);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void deleteRecord(long id) {
        final SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(TABLE_RECORD, RECORD_ID + "=?", new String[]{String.valueOf(id)});
        Log.d(TAG, "deleteNotificationByPreference: deleted " + delete + " rows!");
        db.close();
    }

    public void putRecord(Record record) {
        SQLiteDatabase db = getWritableDatabase();
        if (record.getId() != null) {
            updateRecord(db, record);
        } else {
            record.setId(System.currentTimeMillis());
            addRecord(db, record);
        }
    }

    public void putRecord(Record record, boolean isRestored) {
        if (isRestored) {
            addRecord(getWritableDatabase(), record);
        } else {
            putRecord(record);
        }
    }

    private void addRecord(SQLiteDatabase db, Record record) {
        ContentValues values = new ContentValues();
        values.put(RECORD_ID, record.getId());
        values.put(RECORD_NAME, record.getName());
        values.put(RECORD_DATE, record.getDate());
        values.put(RECORD_GROUP, record.getGroup());
        values.put(RECORD_SITE, record.getSite());
        values.put(RECORD_USER, record.getUser());
        values.put(RECORD_PASS, record.getPass());
        values.put(RECORD_IMAGE, record.getImage());
        db.insert(TABLE_RECORD, null, values);
        db.close();
    }

    private void updateRecord(SQLiteDatabase db, Record record) {
        ContentValues values = new ContentValues();
        values.put(RECORD_NAME, record.getName());
        values.put(RECORD_DATE, record.getDate());
        values.put(RECORD_GROUP, record.getGroup());
        values.put(RECORD_SITE, record.getSite());
        values.put(RECORD_USER, record.getUser());
        values.put(RECORD_PASS, record.getPass());
        values.put(RECORD_IMAGE, record.getImage());
        db.update(TABLE_RECORD, values, RECORD_ID + " = ?",
                new String[]{record.getId().toString()});
        db.close();
    }

    private boolean recordExist(SQLiteDatabase db, Long id) {
        String Query = "Select * from " + TABLE_RECORD + " where " + RECORD_ID + " = " + id + " ;";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<Record> getRecords() {
        ArrayList<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_RECORD, new String[]{RECORD_ID, RECORD_NAME,
                            RECORD_DATE, RECORD_GROUP, RECORD_SITE,
                            RECORD_USER, RECORD_PASS, RECORD_IMAGE}, null,
                    null, null, null, RECORD_NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    Record record = new Record();
                    record.setId(cursor.getLong(0));
                    record.setName(cursor.getString(1));
                    record.setDate(cursor.getLong(2));
                    record.setGroup(cursor.getString(3));
                    record.setSite(cursor.getString(4));
                    record.setUser(cursor.getString(5));
                    record.setPass(cursor.getString(6));
                    record.setImage(cursor.getString(7));
                    records.add(record);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getRecords(): ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return records;
    }
}
