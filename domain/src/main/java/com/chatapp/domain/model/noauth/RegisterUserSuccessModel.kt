package com.chatapp.domain.model.noauth

data class RegisterUserSuccessModel(
    val refreshToken: String?,
    val accessToken: String?,
    val userId: Int,
)
