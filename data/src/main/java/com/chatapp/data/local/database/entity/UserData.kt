package com.chatapp.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")
data class UserData(
    @PrimaryKey val id: Int,
    val name: String?,
    val username: String,
    val birthday: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val status: String?,
    val avatar: String?,
    val last: String?,
    val online: Boolean,
    val created: String,
    val phone: String,
    val completedTask: Int,
    val aboutMe: String?,
    val zodiacSign: String?,
    @Embedded val avatars: AvatarsEntity?
) {
    @Entity
    data class AvatarsEntity(
        val mediumAvatar: String,
        val bigAvatar: String,
        val miniAvatar: String,
    )
}
