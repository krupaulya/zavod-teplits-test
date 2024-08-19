package com.chatapp.presentation.profile

import com.chatapp.core.UIEvent

sealed class ProfileUIEvent : UIEvent {
    data object OnEditClick : ProfileUIEvent()
    data object OnSaveChanges : ProfileUIEvent()
    data class OnCityChange(val city: String) : ProfileUIEvent()
    data class OnBirthDateChange(val birthDate: String) : ProfileUIEvent()
    data class OnZodiacSignChange(val zodiac: String) : ProfileUIEvent()
    data class OnAboutMeChange(val aboutMe: String) : ProfileUIEvent()
}