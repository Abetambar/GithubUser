package com.example.githubuser.service

import com.example.githubuser.BuildConfig
import com.example.githubuser.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users?")
    fun getGithubSearchUser(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getGithubDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getGithubfollowersUser(
        @Path("username") username: String
    ): Call<ArrayList<FollowUserResponse>>

    @GET("users/{username}/following")
    fun getGithubfollowingUser(
        @Path("username") username: String
    ): Call<ArrayList<FollowUserResponse>>

}