package com.chatapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.chatapp.data.local.database.Converters

@Entity(tableName = "chats")
@TypeConverters(Converters::class)
data class ChatsData(
    @PrimaryKey val id: Int,
    val writer: String,
    val texts: List<Message>
) {
    data class Message(
        val message: String,
        val time: String
    )
}





