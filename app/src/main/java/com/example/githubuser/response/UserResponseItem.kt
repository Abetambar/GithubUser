package com.example.githubuser.response

import com.google.gson.annotations.SerializedName

data class UserResponseItem(

    @SerializedName("login")
    val login: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("avatar_url")
    val avatarUrl: String,
)
