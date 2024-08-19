package com.chatapp.domain.model

data class ChatsModel(
    val id: Int,
    val writer: String,
    val texts: List<Message>
) {
    data class Message(
        val message: String,
        val time: String
    )
}