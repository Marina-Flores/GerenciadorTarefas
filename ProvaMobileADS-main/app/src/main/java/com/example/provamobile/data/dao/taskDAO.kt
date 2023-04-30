package com.example.provamobile.data.dao

import androidx.room.*
import com.example.provamobile.data.models.task
import com.example.provamobile.data.models.user

@Dao
interface taskDAO {

    @Insert
    fun insert(task: task)

    @Delete
    fun delete(task: task)

    @Update
    fun update(task: task)

    @Query("SELECT * FROM task")
    fun getAll() : List<task>

    @Query("SELECT * FROM task JOIN user on :user_uid = task.user_uid")
    fun getByUser(user_uid : Int) : Map<user, List<task>>

    @Query("SELECT * FROM task WHERE uid = :id")
    fun getById(id: Int) : task?

    @Query("DELETE FROM task")
    fun deleteAllTasks()
}