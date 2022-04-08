package com.example.githubuser.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.response.FollowUserResponse
import com.example.githubuser.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {

    private val _githubFollowingUser = MutableLiveData<ArrayList<FollowUserResponse>>()
    private val githubFollowUser: LiveData<ArrayList<FollowUserResponse>> = _githubFollowingUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setFollowingUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubfollowingUser(username)
        client.enqueue(object: Callback<ArrayList<FollowUserResponse>>{
            override fun onResponse(
                call: Call<ArrayList<FollowUserResponse>>,
                response: Response<ArrayList<FollowUserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _githubFollowingUser.value = response.body()
                }else {
                    val errorMessage = when (response.code()) {
                        401 -> "statusCode : Bad Request"
                        403 -> "statusCode : Forbidden"
                        404 -> "statusCode : Not Found"
                        else -> "statusCode : ${response.message()}"
                    }
                    Log.d(TAG, "onFailure $errorMessage")
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowUserResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure ${t.message}")
            }

        })

    }

    fun getFollowingUser():LiveData<ArrayList<FollowUserResponse>>{
        return githubFollowUser
    }

    companion object{
        private const val TAG = "FollowingViewModel"
    }
}