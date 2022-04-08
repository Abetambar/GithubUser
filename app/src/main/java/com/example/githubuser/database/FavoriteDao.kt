package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: FavoriteUser)

    @Query("SELECT * FROM favorite_user_table")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favorite_user_table WHERE id = :id")
    fun checkUser(id: Int): Int

    @Query("DElETE FROM favorite_user_table WHERE id = :id")
    fun delete(id: Int): Int

}