package com.chatapp.presentation.profile

import androidx.lifecycle.viewModelScope
import com.chatapp.core.BaseViewModel
import com.chatapp.domain.Repository
import com.chatapp.presentation.mapper.toUpdateUserModel
import com.chatapp.presentation.model.UserDataUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<ProfileUIState, ProfileUIEvent>() {

    init {
        getCurrentUser()
    }

    override val initialState: ProfileUIState
        get() = ProfileUIState(
            userData = UserDataUIModel(
                name = "",
                nickname = "",
                phoneNumber = "",
                city = "",
                birthDate = "",
                zodiacSign = "",
                aboutMe = "",
                instagram = "",
                vk = "",
                status = "",
                avatar = ""
            ),
            isLoading = true,
            editEnabled = false
        )

    override fun handleUserIntent(intent: ProfileUIEvent) {
        when (intent) {
            is ProfileUIEvent.OnAboutMeChange -> changeAboutMe(intent.aboutMe)
            is ProfileUIEvent.OnBirthDateChange -> changeBirthDate(intent.birthDate)
            is ProfileUIEvent.OnCityChange -> changeCity(intent.city)
            is ProfileUIEvent.OnZodiacSignChange -> changeZodiac(intent.zodiac)
            ProfileUIEvent.OnEditClick -> updateEditingState(true)
            ProfileUIEvent.OnSaveChanges -> saveChanges()
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val localUser = repository.getUser()
            if (localUser != null) {
                updateLoadingState(false)
                val user = uiState.value.userData.copy(
                    nickname = localUser.username,
                    phoneNumber = localUser.phone,
                    city = localUser.city,
                    birthDate = localUser.birthday,
                    avatar = localUser.avatar
                )
                updateState { oldState ->
                    oldState.copy(
                        userData = user
                    )
                }
            } else {
                repository.getCurrentUser()
                    .handleApiResultFlow(
                        scope = viewModelScope,
                        onSuccess = { success ->
                            updateLoadingState(false)
                            val user = uiState.value.userData.copy(
                                name = success.name.orEmpty(),
                                nickname = success.username,
                                phoneNumber = success.phone,
                                city = success.city,
                                birthDate = success.birthday,
                                avatar = success.avatar,
                                vk = success.vk,
                                instagram = success.instagram,
                                status = success.status
                            )
                            viewModelScope.launch { repository.insertUser(success) }
                            updateState { oldState ->
                                oldState.copy(
                                    userData = user
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

    private fun saveChanges() {
        viewModelScope.launch {
            val newUser = uiState.value.userData
            repository.updateUser(updateUserModel = newUser.toUpdateUserModel())
                .handleApiResultFlow(
                    scope = viewModelScope,
                    onSuccess = {
                        updateLoadingState(false)
                        viewModelScope.launch { repository.localUpdateUser(
                            birthday = newUser.birthDate,
                            city = newUser.city,
                            avatar = newUser.avatar,
                            instagram = newUser.instagram,
                            vk = newUser.vk,
                            status = newUser.status,
                            aboutMe = newUser.aboutMe
                        ) }
                        updateEditingState(false)
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

    private fun updateLoadingState(isLoading: Boolean) {
        updateState { oldState ->
            oldState.copy(isLoading = isLoading)
        }
    }

    private fun updateEditingState(enabled: Boolean) {
        updateState { oldState ->
            oldState.copy(editEnabled = enabled)
        }
    }

    private fun changeBirthDate(birthDate: String) {
        val userData = uiState.value.userData.copy(birthDate = birthDate)
        updateState { oldState -> oldState.copy(userData = userData) }
    }

    private fun changeAboutMe(text: String) {
        val userData = uiState.value.userData.copy(aboutMe = text)
        updateState { oldState -> oldState.copy(userData = userData) }
    }

    private fun changeCity(city: String) {
        val userData = uiState.value.userData.copy(city = city)
        updateState { oldState -> oldState.copy(userData = userData) }
    }

    private fun changeZodiac(zodiacSign: String) {
        val userData = uiState.value.userData.copy(zodiacSign = zodiacSign)
        updateState { oldState -> oldState.copy(userData = userData) }
    }
}