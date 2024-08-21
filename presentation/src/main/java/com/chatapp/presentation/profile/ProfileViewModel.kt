package com.chatapp.presentation.profile

import androidx.lifecycle.viewModelScope
import com.chatapp.core.BaseViewModel
import com.chatapp.domain.Repository
import com.chatapp.presentation.mapper.toUpdateUserModel
import com.chatapp.presentation.model.UserDataUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

            is ProfileUIEvent.OnDialogChange -> updateState { oldState ->
                oldState.copy(isDatePicker = intent.isOpen)
            }

            is ProfileUIEvent.OnDateSelected -> changeBirthdate(intent.date)
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val localUser = repository.getUser()
            if (localUser != null) {
                updateLoadingState(false)
                val zodiacSign = if (localUser.zodiacSign.isNullOrEmpty()) localUser.birthday?.let { getZodiacSign(it) } else localUser.zodiacSign
                val user = uiState.value.userData.copy(
                    name = localUser.name.orEmpty(),
                    nickname = localUser.username,
                    phoneNumber = localUser.phone,
                    city = localUser.city,
                    birthDate = localUser.birthday,
                    avatar = if (localUser.avatar != null)
                        UserDataUIModel.Avatar(
                            filename = "",
                            base64String = localUser.avatar
                        ) else null,
                    zodiacSign = zodiacSign,
                    aboutMe = localUser.aboutMe,
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
                                zodiacSign = newUser.zodiacSign,
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

    private fun changeAboutMe(text: String) {
        val userData = uiState.value.userData.copy(aboutMe = text)
        updateState { oldState -> oldState.copy(userData = userData) }
    }

    private fun changeCity(city: String) {
        val userData = uiState.value.userData.copy(city = city)
        updateState { oldState -> oldState.copy(userData = userData) }
    }

    private fun changeBirthdate(date: Long?) {
        val selectedDate = formatMillisToDate(date)
        val zodiac = getZodiacSign(date)
        val newUser = uiState.value.userData.copy(
            birthDate = selectedDate,
            zodiacSign = zodiac
        )
        updateState { oldState ->
            oldState.copy(
                userData = newUser
            )
        }
    }

    private fun formatMillisToDate(dateMillis: Long?): String {
        if (dateMillis != null) {
            val date = Date(dateMillis)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return format.format(date)
        }
        return ""
    }

    private fun getZodiacSign(dateMillis: Long?): String {
        if (dateMillis == null) return ""
        val date = Date(dateMillis)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        return zodiacSign(day, month)
    }

    private fun getZodiacSign(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        val day = date.dayOfMonth
        val month = date.monthValue
        return zodiacSign(day, month)
    }

    private fun zodiacSign(day: Int, month: Int): String {
        return when {
            (day >= 21 && month == 3) || (day <= 19 && month == 4) -> "Овен"
            (day >= 20 && month == 4) || (day <= 20 && month == 5) -> "Телец"
            (day >= 21 && month == 5) || (day <= 20 && month == 6) -> "Близнецы"
            (day >= 21 && month == 6) || (day <= 22 && month == 7) -> "Рак"
            (day >= 23 && month == 7) || (day <= 22 && month == 8) -> "Лев"
            (day >= 23 && month == 8) || (day <= 22 && month == 9) -> "Дева"
            (day >= 23 && month == 9) || (day <= 22 && month == 10) -> "Весы"
            (day >= 23 && month == 10) || (day <= 21 && month == 11) -> "Скорпион"
            (day >= 22 && month == 11) || (day <= 21 && month == 12) -> "Стрелец"
            (day >= 22 && month == 12) || (day <= 19 && month == 1) -> "Козерог"
            (day >= 20 && month == 1) || (day <= 18 && month == 2) -> "Водолей"
            (day >= 19 && month == 2) || (day <= 20 && month == 3) -> "Рыбы"
            else -> ""
        }
    }
}