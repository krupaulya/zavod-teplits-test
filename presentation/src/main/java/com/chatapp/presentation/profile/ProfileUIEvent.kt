package com.chatapp.presentation.profile

import android.content.ContentResolver
import android.net.Uri
import com.chatapp.core.UIEvent

sealed class ProfileUIEvent : UIEvent {
    data object OnEditClick : ProfileUIEvent()
    data object OnSaveChanges : ProfileUIEvent()
    data class OnCityChange(val city: String) : ProfileUIEvent()
    data class OnAboutMeChange(val aboutMe: String) : ProfileUIEvent()
    data class OnSuccessImageUpload(val contentResolver: ContentResolver, val imageUri: Uri) : ProfileUIEvent()
    data class OnDialogChange(val isOpen: Boolean) : ProfileUIEvent()
    data class OnDateSelected(val date: Long?) : ProfileUIEvent()
}