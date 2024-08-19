package com.chatapp.presentation.registration

import com.chatapp.core.UIEvent

sealed class RegistrationUIEvent : UIEvent {

    data class OnAddArguments(val arguments: String): RegistrationUIEvent()
    data class OnNameChanged(val name: String): RegistrationUIEvent()
    data class OnUserNameChanged(val userName: String): RegistrationUIEvent()
    data object RegisterUser: RegistrationUIEvent()
    data object OnNavigate: RegistrationUIEvent()
}