package com.chatapp.data.remote.mapper

import com.chatapp.data.remote.model.noauth.CheckAuthCodeRequest
import com.chatapp.data.remote.model.noauth.CheckAuthCodeResponse
import com.chatapp.domain.model.noauth.CheckAuthCodeModel
import com.chatapp.domain.model.noauth.CheckAuthCodeSuccessModel

fun CheckAuthCodeModel.toRequest(): CheckAuthCodeRequest = CheckAuthCodeRequest(
    phoneNumber = phoneNumber,
    code = code
)

fun mapToCheckCodeSuccessModel(response: CheckAuthCodeResponse): CheckAuthCodeSuccessModel {
    return CheckAuthCodeSuccessModel(
        refreshToken = response.refreshToken,
        accessToken = response.accessToken,
        userId = response.userId,
        isUserExists = response.isUserExists
    )
}