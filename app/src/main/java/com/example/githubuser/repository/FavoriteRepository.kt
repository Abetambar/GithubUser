package com.example.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.database.FavoriteDao
import com.example.githubuser.database.FavoriteRoomDatabase
import com.example.githubuser.database.FavoriteUser

class FavoriteRepository(application: Application) {
    private var mFavoriteDb: FavoriteRoomDatabase? = FavoriteRoomDatabase.getDatabase(application)
    private var mFavoriteDao: FavoriteDao? = mFavoriteDb?.favoriteDao()

    fun getAllFavoriteUser():LiveData<List<FavoriteUser>>? = mFavoriteDao?.getFavoriteUser()

    fun addToFavorite(user: FavoriteUser) = mFavoriteDao?.insert(user)

    fun checkUser(id: Int) = mFavoriteDao?.checkUser(id)

    fun deleteFromFavorite(id: Int) = mFavoriteDao?.delete(id)
}