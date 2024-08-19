package com.chatapp.presentation.profile

import com.chatapp.core.UIState
import com.chatapp.presentation.model.ChatsDataUIModel
import com.chatapp.presentation.model.UserDataUIModel

data class ProfileUIState(
    val userData: UserDataUIModel,
    val isLoading: Boolean,
    val editEnabled: Boolean
) : UIState