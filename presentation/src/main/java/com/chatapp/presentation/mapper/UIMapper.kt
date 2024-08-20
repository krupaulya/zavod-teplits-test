package com.chatapp.presentation.mapper

import com.chatapp.domain.model.ChatsModel
import com.chatapp.domain.model.noauth.CheckAuthCodeModel
import com.chatapp.domain.model.CountryModel
import com.chatapp.domain.model.auth.UpdateUserModel
import com.chatapp.domain.model.noauth.RegisterUserModel
import com.chatapp.domain.model.noauth.PhoneNumberModel
import com.chatapp.presentation.model.ChatsDataUIModel
import com.chatapp.presentation.model.CodeDataUIModel
import com.chatapp.presentation.model.CountryDataUIModel
import com.chatapp.presentation.model.PhoneNumberUIModel
import com.chatapp.presentation.model.RegisterUserUIModel
import com.chatapp.presentation.model.UserDataUIModel

fun PhoneNumberUIModel.toDomain(): PhoneNumberModel = PhoneNumberModel(phoneNumber = phoneNumber)

fun List<CountryModel>.toCountryUIModel(): List<CountryDataUIModel> = this.map { model ->
    CountryDataUIModel(
        name = model.name,
        code = model.code,
        flag = model.flag,
        iso = model.iso,
        mask = model.mask
    )
}

fun CodeDataUIModel.toDomain(): CheckAuthCodeModel = CheckAuthCodeModel(phoneNumber = phoneNumber, code = confirmationCode)

fun RegisterUserUIModel.toDomain(): RegisterUserModel = RegisterUserModel(phoneNumber = phoneNumber, name = name, username = userName)

fun List<ChatsModel>.toChatsUIModel(): List<ChatsDataUIModel> = this.map { model ->
    ChatsDataUIModel(
        id = model.id,
        writer = model.writer,
        texts = model.texts.toUIModel()
    )
}

fun List<ChatsModel.Message>.toUIModel(): List<ChatsDataUIModel.Message> = this.map { model ->
    ChatsDataUIModel.Message(
        message = model.message,
        time = model.time
    )
}

fun UserDataUIModel.toUpdateUserModel(): UpdateUserModel {
    return UpdateUserModel(
        name = this.name,
        username = this.nickname,
        birthday = this.birthDate,
        city = this.city,
        vk = this.vk,
        instagram = this.instagram,
        status = this.status,
        avatar = this.avatar?.let {
            UpdateUserModel.Avatar(
                filename = it.filename,
                base64 = it.base64String
            )
        }
    )
}