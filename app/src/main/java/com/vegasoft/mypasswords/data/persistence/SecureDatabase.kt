package com.vegasoft.mypasswords.data.persistence

import android.content.Context
import androidx.room.*
import com.vegasoft.mypasswords.data.persistence.models.Record
import com.vegasoft.mypasswords.data.persistence.models.User


/**
 * The Room database that contains the Users table
 */
@Database(
    entities = [Record::class, User::class],
    version = 1
)
open abstract class SecureDatabase : RoomDatabase() {

    abstract fun recordsDao(): RecordDao
    abstract fun user(): UserDao


    companion object {

        @Volatile
        private var INSTANCE: SecureDatabase? = null

        fun getInstance(context: Context): SecureDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                    SecureDatabase::class.java, "secure.db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}