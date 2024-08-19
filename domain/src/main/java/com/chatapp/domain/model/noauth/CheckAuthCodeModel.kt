package com.chatapp.domain.model.noauth

data class CheckAuthCodeModel(
    val phoneNumber: String,
    val code: String,
)
