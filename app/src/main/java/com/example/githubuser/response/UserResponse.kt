package com.example.githubuser.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @SerializedName("items")
    val items : ArrayList<UserResponseItem>

)