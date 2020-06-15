package com.vegasoft.mypasswords.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vegasoft.mypasswords.data.persistence.models.Record

@Dao
abstract class RecordDao {
    @Query("SELECT * FROM records WHERE id = :id")
    abstract suspend fun getRecordById(id: String): Record

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertRecord(post: Record)

    @Query("SELECT * FROM records")
    abstract suspend fun getRecords(): List<Record>

    @Query("DELETE FROM records")
    abstract suspend fun deleteAllRecords()

    @Query("DELETE FROM records WHERE id = :id")
    abstract suspend fun deleteRecordById(id: String)
}