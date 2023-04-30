package com.example.provamobile.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(entity = user::class,
               parentColumns = ["uid"],
                childColumns = ["user_uid"])
])
data class task(
    @PrimaryKey(autoGenerate = true)
    var uid : Int?,

    @ColumnInfo(name = "title")
    val title : String?,

    @ColumnInfo(name = "description")
    val description : String?,

    @ColumnInfo(name = "user_uid")
    val userUid : Int
)
