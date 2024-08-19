package com.chatapp.presentation.registration

import com.chatapp.presentation.navigation.Navigation
import com.chatapp.core.UIState

data class RegistrationUIState(
    val phoneNumber: String,
    val name: String,
    val userName: String,
    val isLoading: Boolean,
    val isUserNameValid: Boolean,
    val navigation: Navigation? = null
) : UIState