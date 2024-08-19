package com.chatapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chatapp.data.local.database.entity.ChatsData
import com.chatapp.data.local.database.entity.UserData

@Dao
interface ChatsDao {

    @Query("SELECT * FROM chats")
    suspend fun getAllChats(): List<ChatsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<ChatsData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserData)

    @Query("SELECT * FROM user WHERE id=:id")
    suspend fun getUser(id: Int): UserData?

    @Query("UPDATE user SET birthday = :birthday, city = :city, vk = :vk, instagram = :instagram, status = :status, avatar = :avatar, aboutMe = :aboutMe WHERE id = :id")
    suspend fun updateUser(
        id: Int,
        birthday: String?,
        city: String?,
        vk: String?,
        instagram: String?,
        status: String?,
        avatar: String?,
        aboutMe: String?
    )
}