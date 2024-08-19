package com.chatapp.data.remote.model.noauth

import com.google.gson.annotations.SerializedName

data class CheckAuthCodeRequest(
    @SerializedName("phone") val phoneNumber: String,
    @SerializedName("code") val code: String,
)
