package com.example.githubuser.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.database.FavoriteUser
import com.example.githubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavoriteUser():LiveData<List<FavoriteUser>>?{
        return mFavoriteRepository.getAllFavoriteUser()
    }
}