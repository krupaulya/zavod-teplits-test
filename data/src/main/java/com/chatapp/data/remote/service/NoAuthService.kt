package com.chatapp.data.remote.service

import com.chatapp.data.remote.model.noauth.CheckAuthCodeRequest
import com.chatapp.data.remote.model.noauth.CheckAuthCodeResponse
import com.chatapp.data.remote.model.noauth.RegisterUserRequest
import com.chatapp.data.remote.model.noauth.RegisterUserResponse
import com.chatapp.data.remote.model.noauth.SendAuthCodeRequest
import com.chatapp.data.remote.model.noauth.SendAuthCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NoAuthService {

    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: SendAuthCodeRequest): Response<SendAuthCodeResponse>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: CheckAuthCodeRequest): Response<CheckAuthCodeResponse>

    @POST("/api/v1/users/register/")
    suspend fun registerUser(@Body request: RegisterUserRequest): Response<RegisterUserResponse>
}