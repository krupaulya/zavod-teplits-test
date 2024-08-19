package com.chatapp.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatsDataUIModel(
    val id: Int,
    val writer: String,
    val texts: List<Message>
) {
    @Serializable
    data class Message(
        val message: String,
        val time: String
    )
}
