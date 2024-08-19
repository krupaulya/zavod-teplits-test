package com.chatapp.data.remote.mapper

import com.chatapp.data.remote.model.auth.UpdateUserRequest
import com.chatapp.data.remote.model.auth.UserResponse
import com.chatapp.domain.model.auth.UpdateUserModel
import com.chatapp.domain.model.auth.UserModel

fun mapToUserModel(response: UserResponse): UserModel {
    return UserModel(
        name = response.profileData.name,
        username = response.profileData.username,
        birthday = response.profileData.birthday,
        city = response.profileData.city,
        vk = response.profileData.vk,
        instagram = response.profileData.instagram,
        status = response.profileData.status,
        avatar = response.profileData.avatar,
        id = response.profileData.id,
        last = response.profileData.last,
        online = response.profileData.online,
        created = response.profileData.created,
        phone = response.profileData.phone,
        completedTask = response.profileData.completedTask,
        avatars = response.profileData.avatars?.toAvatarsModel()
    )
}

fun UserResponse.ProfileData.Avatars.toAvatarsModel(): UserModel.Avatars {
    return UserModel.Avatars(
        avatar = this.avatar,
        bigAvatar = this.bigAvatar,
        miniAvatar = this.miniAvatar
    )
}

fun mapToAvatarsModel(avatars: UserResponse.ProfileData.Avatars?): UserModel.Avatars {
    return UserModel.Avatars(
        avatar = avatars?.avatar.orEmpty(),
        bigAvatar = avatars?.bigAvatar.orEmpty(),
        miniAvatar = avatars?.miniAvatar.orEmpty()
    )
}

fun UpdateUserModel.toRequest(): UpdateUserRequest {
    return UpdateUserRequest(
        name = this.name,
        username = this.username,
        birthday = this.birthday,
        city = this.city,
        vk = this.vk,
        instagram = this.instagram,
        status = this.status,
        avatar = this.avatar?.toRequest()
    )
}

fun UpdateUserModel.Avatar.toRequest(): UpdateUserRequest.Avatar {
    return UpdateUserRequest.Avatar(
        filename = this.filename,
        base64 = this.base64
    )
}