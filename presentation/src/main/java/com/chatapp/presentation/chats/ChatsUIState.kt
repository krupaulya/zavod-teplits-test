package com.chatapp.presentation.chats

import com.chatapp.presentation.navigation.Navigation
import com.chatapp.core.UIState
import com.chatapp.presentation.model.ChatsDataUIModel

data class ChatsUIState(
    val chats: List<ChatsDataUIModel>,
    val isLoading: Boolean,
    val navigation: Navigation? = null
) : UIState