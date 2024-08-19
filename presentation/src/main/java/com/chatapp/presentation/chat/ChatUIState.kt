package com.chatapp.presentation.chat

import com.chatapp.core.UIState
import com.chatapp.presentation.model.ChatsDataUIModel

data class ChatUIState(
    val chat: ChatsDataUIModel,
    val isLoading: Boolean
) : UIState