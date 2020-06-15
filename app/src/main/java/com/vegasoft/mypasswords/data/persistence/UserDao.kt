package com.vegasoft.mypasswords.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vegasoft.mypasswords.data.persistence.models.User

@Dao
abstract class UserDao {
    @Query("SELECT * FROM users")
    abstract suspend fun getUser(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUser(user: User)

    @Query("DELETE FROM users")
    abstract suspend fun deleteUser()
}