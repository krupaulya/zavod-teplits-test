package com.chatapp.data.remote.model.noauth

import com.google.gson.annotations.SerializedName

data class RegisterUserRequest(
    @SerializedName("phone") val phoneNumber: String,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
)
