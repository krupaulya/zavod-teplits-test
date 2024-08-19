package com.chatapp.domain.model.noauth

data class RegisterUserModel(
    val phoneNumber: String,
    val name: String,
    val username: String
)
