package com.chatapp.presentation.authorization

import android.content.Context
import android.telephony.TelephonyManager
import androidx.lifecycle.viewModelScope
import com.chatapp.core.BaseViewModel
import com.chatapp.domain.Repository
import com.chatapp.presentation.mapper.toCountryUIModel
import com.chatapp.presentation.mapper.toDomain
import com.chatapp.presentation.model.CodeDataUIModel
import com.chatapp.presentation.model.CountryDataUIModel
import com.chatapp.presentation.model.PhoneNumberUIModel
import com.chatapp.presentation.navigation.ChatsScreen
import com.chatapp.presentation.navigation.Navigation
import com.chatapp.presentation.navigation.RegistrationScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<AuthorizationUIState, AuthorizationUIEvent>() {

    init {
        viewModelScope.launch {
            val countries = repository.getAllCountries()
            updateState { oldState ->
                oldState.copy(
                    countryData = countries.toCountryUIModel(),
                    isLoading = false
                )
            }
        }
    }

    override val initialState: AuthorizationUIState
        get() = AuthorizationUIState(
            phoneNumber = "",
            countryData = emptyList(),
            isLoading = true,
            selectedCountry = null,
            isConfirmationCode = false,
            confirmationCodeValue = "",
            isPhoneNumberValid = true,
            isCodeValid = true
        )

    override fun handleUserIntent(intent: AuthorizationUIEvent) {
        when (intent) {
            AuthorizationUIEvent.SendPhoneNumber -> sendAuthCode()
            is AuthorizationUIEvent.OnCountryChanged -> changeCountry(newCountry = intent.newCountry)
            is AuthorizationUIEvent.CalculateDefaultRegion -> calculateDefaultCountryCode(intent.context)
            is AuthorizationUIEvent.OnPhoneChanged -> changePhoneValue(phoneValue = intent.phone)
            is AuthorizationUIEvent.OnConfirmationCodeChanged -> changeConfirmationCode(newCode = intent.code)
            AuthorizationUIEvent.SendConfirmationCode -> checkAuthCode()
            AuthorizationUIEvent.OnNavigate -> updateNavigation(null)
        }
    }

    private fun sendAuthCode() {
        val maxLength = uiState.value.selectedCountry?.mask?.count { it == '#' || it.isDigit() } ?: 13
        if (uiState.value.phoneNumber.length < maxLength) {
            updateState { oldState -> oldState.copy(isPhoneNumberValid = false) }
        } else {
            updateState { oldState -> oldState.copy(isPhoneNumberValid = true) }
            val request = PhoneNumberUIModel(phoneNumber = uiState.value.phoneNumber)
            viewModelScope.launch {
                repository.sendAuthCode(phoneNumber = request.toDomain())
                    .handleApiResultFlow(
                        scope = viewModelScope,
                        onSuccess = {
                            updateLoadingState(false)
                            updateConfirmationState(true)
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

    private fun checkAuthCode() {
        if (uiState.value.confirmationCodeValue.length < 6) {
            updateState { oldState -> oldState.copy(isCodeValid = false) }
        } else {
            updateState { oldState -> oldState.copy(isCodeValid = true) }
            val request = CodeDataUIModel(
                phoneNumber = uiState.value.phoneNumber,
                confirmationCode = uiState.value.confirmationCodeValue
            )
            viewModelScope.launch {
                repository.checkAuthCode(data = request.toDomain())
                    .handleApiResultFlow(
                        scope = viewModelScope,
                        onSuccess = { success ->
                            updateLoadingState(false)
                            viewModelScope.launch {
                                repository.saveTokens(
                                    refreshToken = success.refreshToken.orEmpty(),
                                    accessToken = success.accessToken.orEmpty()
                                )
                                repository.saveUserId(success.userId)
                                if (success.isUserExists) {
                                    updateNavigation(navigation = ChatsScreen)
                                } else {
                                    updateNavigation(navigation = RegistrationScreen(phoneNumber = uiState.value.phoneNumber))
                                }
                            }
                            updateConfirmationState(false)
                            updateConfirmationCode(code = "")
                        },
                        onError = {
                            updateLoadingState(false)
                            updateConfirmationState(false)
                            updateConfirmationCode(code = "")
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

    private fun updateNavigation(navigation: Navigation?) {
        updateState { oldState ->
            oldState.copy(navigation = navigation)
        }
    }

    private fun updateConfirmationState(isConfirmationCode: Boolean) {
        updateState { oldState ->
            oldState.copy(isConfirmationCode = isConfirmationCode)
        }
    }

    private fun updateConfirmationCode(code: String) {
        updateState { oldState ->
            oldState.copy(confirmationCodeValue = code)
        }
    }

    private fun calculateDefaultCountryCode(context: Context) {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkCountryIso = telephonyManager.networkCountryIso?.uppercase(Locale.getDefault())
        val defaultRegion = networkCountryIso ?: Locale.getDefault().country
        updateState { oldState ->
            oldState.copy(selectedCountry = uiState.value.countryData.find { it.iso == defaultRegion })
        }
        updateState { oldState ->
            oldState.copy(phoneNumber = "+${uiState.value.selectedCountry?.code.orEmpty()}")
        }
    }

    private fun changePhoneValue(phoneValue: String) {
        val maxLength = uiState.value.selectedCountry?.mask?.count { it == '#' || it.isDigit() } ?: 13
        val cleanInput = phoneValue.filter { it.isDigit() || it == '+' }
        val onlyDigit = phoneValue.filter { it.isDigit() }
        uiState.value.countryData.forEach {
            if (onlyDigit == it.code) {
                updateState { oldState -> oldState.copy(selectedCountry = it) }
            }
        }
        val newFilteredValue = cleanInput.ifEmpty { "+$cleanInput" }
        if (cleanInput.length - 1 < maxLength) {
            updateState { oldState ->
                oldState.copy(phoneNumber = newFilteredValue)
            }
        }
    }

    private fun changeCountry(newCountry: CountryDataUIModel) {
        updateState { oldState ->
            oldState.copy(
                selectedCountry = newCountry,
                phoneNumber = "+${newCountry.code}"
            )
        }
    }

    private fun changeConfirmationCode(newCode: String) {
        val cleanInput = newCode.filter { it.isDigit() }
        if (cleanInput.length <= 6) {
            updateConfirmationCode(code = cleanInput)
        }
    }
}