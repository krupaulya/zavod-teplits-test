package com.chatapp.domain.model.noauth

data class CheckAuthCodeSuccessModel(
    val refreshToken: String?,
    val accessToken: String?,
    val userId: Int,
    val isUserExists: Boolean
)
