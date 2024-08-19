package com.chatapp.data.remote.mapper

import com.chatapp.data.remote.model.noauth.RegisterUserRequest
import com.chatapp.data.remote.model.noauth.RegisterUserResponse
import com.chatapp.domain.model.noauth.RegisterUserModel
import com.chatapp.domain.model.noauth.RegisterUserSuccessModel

fun RegisterUserModel.toRequest(): RegisterUserRequest = RegisterUserRequest(
    phoneNumber = phoneNumber,
    name = name,
    username = username
)

fun mapToRegisterUserSuccessModel(response: RegisterUserResponse): RegisterUserSuccessModel {
    return RegisterUserSuccessModel(
        accessToken = response.accessToken,
        refreshToken = response.refreshToken,
        userId = response.userId
    )
}