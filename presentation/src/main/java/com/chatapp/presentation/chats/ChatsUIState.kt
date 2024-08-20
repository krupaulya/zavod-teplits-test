package com.chatapp.presentation.chats

import android.graphics.Bitmap
import com.chatapp.presentation.navigation.Navigation
import com.chatapp.core.UIState
import com.chatapp.presentation.model.ChatsDataUIModel

data class ChatsUIState(
    val chats: List<ChatsDataUIModel>,
    val nickname: String,
    val avatar: String?,
    val imageBitmap: Bitmap? = null,
    val isLoading: Boolean,
    val navigation: Navigation? = null
) : UIState