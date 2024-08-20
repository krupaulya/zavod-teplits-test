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
        get() = ChatsUIState(chats = emptyList(), nickname = "", avatar = null, isLoading = true)

    override fun handleUserIntent(intent: ChatsUIEvent) {
        when (intent) {
            ChatsUIEvent.GetCurrentUser -> getCurrentUser()
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val localUser = repository.getUser()
            if (localUser != null) {
                updateLoadingState(false)
                updateState { oldState ->
                    oldState.copy(
                        nickname = localUser.username,
                        avatar = localUser.avatar,
                        imageBitmap = repository.base64ToBitmap(base64String = localUser.avatar)
                    )
                }
            } else {
                repository.getCurrentUser()
                    .handleApiResultFlow(
                        scope = viewModelScope,
                        onSuccess = { success ->
                            updateLoadingState(false)
                            viewModelScope.launch { repository.insertUser(success) }
                            updateState { oldState ->
                                oldState.copy(
                                    nickname = success.username,
                                    avatar = success.avatar,
                                    imageBitmap = repository.base64ToBitmap(base64String = success.avatar)
                                )
                            }
                        },
                        onError = {
                            updateLoadingState(false)
                        },
                        onLoading = {
                            updateLoadingState(true)
                        }
                    )
            }
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        updateState { oldState ->
            oldState.copy(isLoading = isLoading)
        }
    }
}