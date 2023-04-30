package com.example.provamobile.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.provamobile.data.dao.taskDAO
import com.example.provamobile.data.dao.userDAO
import com.example.provamobile.data.models.task
import com.example.provamobile.data.models.user

@Database(entities = [task::class, user::class], version = 3)
abstract class AppDataBase : RoomDatabase() {
    abstract fun taskDao() : taskDAO
    abstract fun userDao() : userDAO
}