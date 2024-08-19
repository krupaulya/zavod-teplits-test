package com.chatapp.data.remote.model.noauth

import com.google.gson.annotations.SerializedName

data class SendAuthCodeRequest(
    @SerializedName("phone") val phoneNumber: String
)
