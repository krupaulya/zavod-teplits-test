package com.chatapp.presentation.authorization

import android.content.Context
import com.chatapp.core.UIEvent
import com.chatapp.presentation.model.CountryDataUIModel

sealed class AuthorizationUIEvent: UIEvent {
    data object SendPhoneNumber: AuthorizationUIEvent()
    data object SendConfirmationCode: AuthorizationUIEvent()
    data class OnCountryChanged(val newCountry: CountryDataUIModel): AuthorizationUIEvent()
    data class CalculateDefaultRegion(val context: Context): AuthorizationUIEvent()
    data class OnPhoneChanged(val phone: String): AuthorizationUIEvent()
    data class OnConfirmationCodeChanged(val code: String): AuthorizationUIEvent()
    data object OnNavigate: AuthorizationUIEvent()
}