package com.example.provamobile.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class user(
    @PrimaryKey(autoGenerate = true)
    var uid : Int?,

    @ColumnInfo(name = "nome")
    val nome : String,

    @ColumnInfo(name = "login")
    val login : String,

    @ColumnInfo(name = "senha")
    val senha : String
)
