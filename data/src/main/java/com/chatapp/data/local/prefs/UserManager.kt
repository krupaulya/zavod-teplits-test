package com.chatapp.data.local.prefs

interface UserManager {
    suspend fun getUserId(): Int?
    suspend fun saveUserId(userId: Int)
}