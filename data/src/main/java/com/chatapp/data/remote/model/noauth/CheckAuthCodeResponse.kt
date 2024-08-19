package com.chatapp.data.remote.model.noauth

import com.google.gson.annotations.SerializedName

data class CheckAuthCodeResponse(
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("is_user_exists") val isUserExists: Boolean
)