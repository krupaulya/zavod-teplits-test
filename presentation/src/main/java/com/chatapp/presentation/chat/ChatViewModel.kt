package com.chatapp.presentation.chat

import com.chatapp.core.BaseViewModel
import com.chatapp.presentation.model.ChatsDataUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : BaseViewModel<ChatUIState, ChatUIEvent>() {

    override val initialState: ChatUIState
        get() = ChatUIState(
            chat = ChatsDataUIModel(
                id = 3,
                writer = "Настя",
                texts = listOf(
                    ChatsDataUIModel.Message("Вы получили документы?", "14:30"),
                    ChatsDataUIModel.Message("Пожалуйста, ознакомьтесь с ними.", "14:35"),
                    ChatsDataUIModel.Message("Можете прислать мне файлы?", "14:40"),
                    ChatsDataUIModel.Message("Заранее спасибо.", "14:50"),
                )
            ),
            isLoading = false
        )

    override fun handleUserIntent(intent: ChatUIEvent) {
        TODO("Not yet implemented")
    }
}