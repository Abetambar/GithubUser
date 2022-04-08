package com.example.githubuser.model

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.helper.SettingPreferences
import com.example.githubuser.response.UserResponse
import com.example.githubuser.response.UserResponseItem
import com.example.githubuser.service.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {

    private val _listGithubUserItem = MutableLiveData<ArrayList<UserResponseItem>>()
    private val listUserItem: LiveData<ArrayList<UserResponseItem>> = _listGithubUserItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun setGithubUserItem(query: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubSearchUser(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listGithubUserItem.value = response.body()?.items
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

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getGithubUserItem():LiveData<ArrayList<UserResponseItem>>{
        return listUserItem
    }


    companion object{
        private const val TAG = "MainViewModel"
    }
}