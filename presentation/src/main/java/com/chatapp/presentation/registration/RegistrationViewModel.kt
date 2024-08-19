package com.chatapp.presentation.registration

import androidx.lifecycle.viewModelScope
import com.chatapp.core.BaseViewModel
import com.chatapp.domain.Repository
import com.chatapp.presentation.mapper.toDomain
import com.chatapp.presentation.model.RegisterUserUIModel
import com.chatapp.presentation.navigation.ChatsScreen
import com.chatapp.presentation.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<RegistrationUIState, RegistrationUIEvent>() {

    override val initialState: RegistrationUIState
        get() = RegistrationUIState(phoneNumber = "", name = "", userName = "", isLoading = false, isUserNameValid = true)

    override fun handleUserIntent(intent: RegistrationUIEvent) {
        when (intent) {
            is RegistrationUIEvent.OnAddArguments -> addArguments(arguments = intent.arguments)
            is RegistrationUIEvent.OnNameChanged -> changeName(name = intent.name)
            is RegistrationUIEvent.OnUserNameChanged -> changeUserName(userName = intent.userName)
            RegistrationUIEvent.RegisterUser -> registerUser()
            RegistrationUIEvent.OnNavigate -> updateNavigation(null)
        }
    }

    private fun addArguments(arguments: String) {
        updateState { oldState ->
            oldState.copy(phoneNumber = arguments)
        }
    }

    private fun changeName(name: String) {
        val cleanName = name.filter { it.isLetter() }
        updateState { oldState -> oldState.copy(name = cleanName) }
    }

    private fun changeUserName(userName: String) {
        updateState { oldState -> oldState.copy(userName = userName) }
    }

    private fun registerUser() {
        val maskPattern = Regex("[A-Za-z0-9\\-_]*")
        if (uiState.value.userName.matches(maskPattern) && uiState.value.userName.length <= 26) {
            updateState { oldState -> oldState.copy(isUserNameValid = true) }
            val request = RegisterUserUIModel(
                phoneNumber = uiState.value.phoneNumber,
                name = uiState.value.name,
                userName = uiState.value.userName
            )
            viewModelScope.launch {
                repository.registerUser(userData = request.toDomain())
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
                            }
                            updateNavigation(navigation = ChatsScreen)
                        },
                        onError = {
                            updateLoadingState(false)
                        },
                        onLoading = {
                            updateLoadingState(true)
                        }
                    )
            }
        } else {
            updateState { oldState -> oldState.copy(isUserNameValid = false) }
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
}