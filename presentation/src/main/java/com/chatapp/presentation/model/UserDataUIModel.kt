package com.chatapp.presentation.model

data class UserDataUIModel(
    val name: String,
    val nickname: String,
    val phoneNumber: String,
    val city: String?,
    val birthDate: String?,
    val zodiacSign: String?,
    val aboutMe: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatar: Avatar?
) {
    data class Avatar(
        val filename: String,
        val base64String: String?
    )
}
