package com.chatapp.presentation.chats

import androidx.lifecycle.viewModelScope
import com.chatapp.core.BaseViewModel
import com.chatapp.domain.Repository
import com.chatapp.presentation.mapper.toChatsUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<ChatsUIState, ChatsUIEvent>() {

    init {
        viewModelScope.launch {
            val chats = repository.getAllChats()
            updateState { oldState ->
                oldState.copy(
                    chats = chats.toChatsUIModel(),
                    isLoading = false
                )
            }
        }
    }

    override val initialState: ChatsUIState
        get() = ChatsUIState(chats = emptyList(), isLoading = true)

    override fun handleUserIntent(intent: ChatsUIEvent) {
    }
}