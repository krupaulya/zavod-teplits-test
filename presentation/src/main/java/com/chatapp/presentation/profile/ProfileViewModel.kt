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
                avatar = null
            ),
            isLoading = true,
            editEnabled = false
        )

    override fun handleUserIntent(intent: ProfileUIEvent) {
        when (intent) {
            is ProfileUIEvent.OnAboutMeChange -> changeAboutMe(intent.aboutMe)
            is ProfileUIEvent.OnBirthDateChange -> changeBirthDate(intent.birthDate)
            is ProfileUIEvent.OnCityChange -> changeCity(intent.city)
            ProfileUIEvent.OnEditClick -> updateEditingState(true)
            ProfileUIEvent.OnSaveChanges -> saveChanges()
            is ProfileUIEvent.OnSuccessImageUpload -> {
                val base64String = repository.getBase64FromUri(
                    contentResolver = intent.contentResolver, uri = intent.imageUri
                )
                val imageBitmap = repository.base64ToBitmap(base64String = base64String)
                updateState { oldState ->
                    val userData = uiState.value.userData.copy(
                        avatar = UserDataUIModel.Avatar(
                            filename = intent.imageUri.toString(),
                            base64String = base64String,
                        )
                    )
                    oldState.copy(
                        userData = userData,
                        imageBitmap = imageBitmap
                    )
                }
            }
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
                    avatar = UserDataUIModel.Avatar(
                        filename = "",
                        base64String = localUser.avatar
                    )
                )
                updateState { oldState ->
                    oldState.copy(
                        userData = user,
                        imageBitmap = repository.base64ToBitmap(localUser.avatar)
                    )
                }
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
                        viewModelScope.launch {
                            repository.localUpdateUser(
                                birthday = newUser.birthDate,
                                city = newUser.city,
                                avatar = newUser.avatar?.base64String,
                                instagram = newUser.instagram,
                                vk = newUser.vk,
                                status = newUser.status,
                                aboutMe = newUser.aboutMe
                            )
                        }
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
}