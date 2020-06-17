package com.vegasoft.mypasswords.data.persistence.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class Record(@PrimaryKey
                  @ColumnInfo(name = "id")
                  val id: String,
                  @ColumnInfo(name = "userId")
                  val userId: String,
                  @ColumnInfo(name = "data")
                  val data: String,
                  @ColumnInfo(name = "title")
                  val title: String,
                  @ColumnInfo(name = "image")
                  val image: String
)