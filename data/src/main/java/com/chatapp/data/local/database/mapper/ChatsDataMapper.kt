package com.chatapp.data.local.database.mapper

import com.chatapp.data.local.database.entity.ChatsData
import com.chatapp.data.local.database.entity.UserData
import com.chatapp.domain.model.ChatsModel
import com.chatapp.domain.model.auth.UserModel

fun List<ChatsModel>.toChatsData(): List<ChatsData> =
    this.map { model ->
        ChatsData(
            id = model.id,
            writer = model.writer,
            texts = model.texts.toMessageData()
        )
    }

fun List<ChatsData>.toChatsDomain(): List<ChatsModel> = this.map { model ->
    ChatsModel(
        id = model.id,
        writer = model.writer,
        texts = model.texts.toMessageDomain()
    )
}

fun List<ChatsModel.Message>.toMessageData(): List<ChatsData.Message> = this.map { model ->
    ChatsData.Message(
        message = model.message,
        time = model.time
    )
}

fun List<ChatsData.Message>.toMessageDomain(): List<ChatsModel.Message> = this.map { model ->
    ChatsModel.Message(
        message = model.message,
        time = model.time
    )
}

fun mapToUserData(userModel: UserModel): UserData {
    return UserData(
        name = userModel.name,
        username = userModel.username,
        birthday = userModel.birthday,
        city = userModel.city,
        vk = userModel.vk,
        instagram = userModel.instagram,
        status = userModel.status,
        avatar = userModel.avatar,
        id = userModel.id,
        last = userModel.last,
        online = userModel.online,
        created = userModel.created,
        phone = userModel.phone,
        completedTask = userModel.completedTask,
        avatars = userModel.avatars?.let {
            UserData.AvatarsEntity(
                mediumAvatar = it.avatar,
                bigAvatar = it.bigAvatar,
                miniAvatar = it.miniAvatar
            )
        }
    )
}

fun mapToUserModel(userData: UserData?): UserModel? {
    return if (userData!=null) UserModel(
        name = userData.name,
        username = userData.username,
        birthday = userData.birthday,
        city = userData.city,
        vk = userData.vk,
        instagram = userData.instagram,
        status = userData.status,
        avatar = userData.avatar,
        id = userData.id,
        last = userData.last,
        online = userData.online,
        created = userData.created,
        phone = userData.phone,
        completedTask = userData.completedTask,
        avatars = userData.avatars?.let {
            UserModel.Avatars(
                avatar = it.mediumAvatar,
                bigAvatar = it.bigAvatar,
                miniAvatar = it.miniAvatar
            )
        }
    ) else null
}