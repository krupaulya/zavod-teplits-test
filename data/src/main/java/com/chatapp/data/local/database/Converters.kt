package com.chatapp.data.local.database

import androidx.room.TypeConverter
import com.chatapp.data.local.database.entity.ChatsData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromMessageList(value: List<ChatsData.Message>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ChatsData.Message>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMessageList(value: String): List<ChatsData.Message> {
        val gson = Gson()
        val type = object : TypeToken<List<ChatsData.Message>>() {}.type
        return gson.fromJson(value, type)
    }
}