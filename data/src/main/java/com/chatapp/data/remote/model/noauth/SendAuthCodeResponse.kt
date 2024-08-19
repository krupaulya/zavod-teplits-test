package com.chatapp.data.remote.model.noauth

import com.google.gson.annotations.SerializedName

data class SendAuthCodeResponse(
    @SerializedName("is_success") val isSuccess: Boolean
)