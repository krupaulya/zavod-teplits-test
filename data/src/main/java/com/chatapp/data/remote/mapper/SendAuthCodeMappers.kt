package com.chatapp.data.remote.mapper

import com.chatapp.data.remote.model.noauth.SendAuthCodeRequest
import com.chatapp.data.remote.model.noauth.SendAuthCodeResponse
import com.chatapp.domain.model.noauth.AuthCodeSuccessModel
import com.chatapp.domain.model.noauth.PhoneNumberModel

fun PhoneNumberModel.toRequest(): SendAuthCodeRequest = SendAuthCodeRequest(phoneNumber = phoneNumber)

fun mapToAuthCodeSuccessModel(response: SendAuthCodeResponse): AuthCodeSuccessModel {
    return AuthCodeSuccessModel(
        isSuccess = response.isSuccess
    )
}