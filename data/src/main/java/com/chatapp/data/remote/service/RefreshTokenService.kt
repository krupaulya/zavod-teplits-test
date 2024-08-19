package com.chatapp.data.remote.service

import com.chatapp.data.remote.model.refresh.RefreshTokenRequest
import com.chatapp.data.remote.model.refresh.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenService {

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequest): Response<RefreshTokenResponse>
}