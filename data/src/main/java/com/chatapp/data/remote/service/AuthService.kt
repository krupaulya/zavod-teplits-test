package com.chatapp.data.remote.service

import com.chatapp.data.remote.model.auth.UpdateUserRequest
import com.chatapp.data.remote.model.auth.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AuthService {

    @GET("api/v1/users/me")
    suspend fun getCurrentUser(): Response<UserResponse>

    @PUT("api/v1/users/me")
    suspend fun updateCurrentUser(
        @Body updateUser: UpdateUserRequest
    ): Response<UserResponse.ProfileData.Avatars?>
}