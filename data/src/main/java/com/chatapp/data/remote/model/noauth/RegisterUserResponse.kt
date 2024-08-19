package com.chatapp.data.remote.model.noauth

import com.google.gson.annotations.SerializedName

data class RegisterUserResponse(
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("user_id") val userId: Int,
)
