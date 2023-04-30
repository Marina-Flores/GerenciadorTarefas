package com.example.provamobile.data.dao

import androidx.room.*
import com.example.provamobile.data.models.task
import com.example.provamobile.data.models.user

@Dao
interface userDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: user) : Long

    @Query("SELECT * FROM user")
    fun getAll() : List<user>

    @Query("SELECT * FROM user WHERE uid = :id")
    fun getById(id: Int) : user?

    @Query("SELECT * FROM user WHERE login = :login")
    fun getByLogin(login: String) : user?

    @Query("SELECT * FROM user WHERE login = :login AND senha = :senha")
    fun login(login: String, senha: String) : user?
}