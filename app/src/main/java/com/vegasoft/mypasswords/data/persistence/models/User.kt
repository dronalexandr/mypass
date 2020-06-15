package com.vegasoft.mypasswords.data.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(@PrimaryKey
                @ColumnInfo(name = "id")
                val id: String,
                @ColumnInfo(name = "nickname")
                val nickname: String,
                @ColumnInfo(name = "pin")
                val pin: String,
                @ColumnInfo(name = "configJson")
                val configJson: String = "",
                @ColumnInfo(name = "encryption")
                val encryption: String,
                @ColumnInfo(name = "syncronisation")
                val synchronisation: String
)