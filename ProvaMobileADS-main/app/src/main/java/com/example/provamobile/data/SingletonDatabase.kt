package com.example.provamobile.data

import android.content.Context
import androidx.room.Room

object SingletonDatabase {
    private var instance : AppDataBase? = null

    fun getInstance(context: Context) : AppDataBase{
        if(instance == null){
            synchronized(SingletonDatabase::class) {
                instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                "task_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
            }
        }
        return instance!!
    }
}