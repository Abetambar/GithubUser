package com.example.githubuser.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.database.FavoriteUser
import com.example.githubuser.repository.FavoriteRepository
import com.example.githubuser.response.UserDetailResponse
import com.example.githubuser.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val _githubUserDetail = MutableLiveData<UserDetailResponse>()
    private val userDetail: LiveData<UserDetailResponse> = _githubUserDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setGithubUserDetail(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubDetailUser(query)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _githubUserDetail.value = response.body()
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "statusCode : Bad Request"
                        403 -> "statusCode : Forbidden"
                        404 -> "statusCode : Not Found"
                        else -> "statusCode : ${response.message()}"
                    }
                    Log.d(TAG, "onFailure $errorMessage")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.d(TAG, "onFailure ${t.message}")
            }

        })
    }

    fun getGithubUserDetail(): LiveData<UserDetailResponse> {
        return userDetail
    }

    fun addToFavorite(id: Int, username: String, avatarUrl: String) {
        executorService.execute {
            val favorite = FavoriteUser(
                id, username, avatarUrl
            )
            mFavoriteRepository.addToFavorite(favorite)
        }
    }

    fun checkUser(id: Int) = mFavoriteRepository.checkUser(id)

    fun deleteFromFavorite(id: Int) {
        executorService.execute {
            mFavoriteRepository.deleteFromFavorite(id)
        }
    }

    companion object {
        private const val TAG = "UserDetailViewModel"
    }
}