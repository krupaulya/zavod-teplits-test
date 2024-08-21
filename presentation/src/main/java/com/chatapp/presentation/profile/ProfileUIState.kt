package com.chatapp.presentation.profile

import android.graphics.Bitmap
import com.chatapp.core.UIState
import com.chatapp.presentation.model.UserDataUIModel

data class ProfileUIState(
    val userData: UserDataUIModel,
    val imageBitmap: Bitmap? = null,
    val isLoading: Boolean,
    val isDatePicker: Boolean = false,
    val editEnabled: Boolean
) : UIState