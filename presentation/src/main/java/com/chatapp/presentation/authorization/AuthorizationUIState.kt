package com.chatapp.presentation.authorization

import androidx.compose.runtime.Immutable
import com.chatapp.presentation.navigation.Navigation
import com.chatapp.core.UIState
import com.chatapp.presentation.model.CountryDataUIModel

@Immutable
data class AuthorizationUIState(
    val phoneNumber: String,
    val countryData: List<CountryDataUIModel>,
    val isLoading: Boolean,
    val selectedCountry: CountryDataUIModel?,
    val isConfirmationCode: Boolean,
    val confirmationCodeValue: String,
    val navigation: Navigation? = null,
    val isPhoneNumberValid: Boolean,
    val isCodeValid: Boolean
) : UIState
