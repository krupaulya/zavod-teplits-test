package com.chatapp.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.chatapp.presentation.components.CommonButton
import com.chatapp.presentation.components.CommonLoadingView
import com.chatapp.presentation.components.CommonTextField
import com.chatapp.presentation.components.CommonTopBar
import com.chatapp.presentation.components.UserImage
import com.chatapp.ui.R
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreenContent(
        uiState = uiState,
        onCityChange = { viewModel.sendUIEvent(ProfileUIEvent.OnCityChange(it)) },
        onBirthDateChange = { viewModel.sendUIEvent(ProfileUIEvent.OnBirthDateChange(it)) },
        onZodiacSignChange = { viewModel.sendUIEvent(ProfileUIEvent.OnZodiacSignChange(it)) },
        onAboutMeChange = { viewModel.sendUIEvent(ProfileUIEvent.OnAboutMeChange(it)) },
        onEditProfileClick = { viewModel.sendUIEvent(ProfileUIEvent.OnEditClick) },
        onSaveChanges = { viewModel.sendUIEvent(ProfileUIEvent.OnSaveChanges) },
        onBackClick = { navController.navigateUp() }
    )
}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileUIState,
    onCityChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onZodiacSignChange: (String) -> Unit,
    onAboutMeChange: (String) -> Unit,
    onEditProfileClick: () -> Unit,
    onSaveChanges: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopBar(
                navIcon = Icons.AutoMirrored.Filled.ArrowBack,
                actionIcon = Icons.Filled.Edit,
                text = "Профиль",
                onActionClick = { onEditProfileClick() },
                onNavIconClick = { onBackClick() })
        },
        containerColor = Navy,
        contentColor = LightYellow
    ) { paddingValues ->
        ProfileContent(
            uiState = uiState,
            onCityChange = onCityChange,
            onBirthDateChange = onBirthDateChange,
            onZodiacSignChange = onZodiacSignChange,
            onAboutMeChange = onAboutMeChange,
            onSaveChanges = onSaveChanges,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ProfileContent(
    uiState: ProfileUIState,
    onCityChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onZodiacSignChange: (String) -> Unit,
    onAboutMeChange: (String) -> Unit,
    onSaveChanges: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            UserImage(
                modifier = Modifier
                    .size(100.dp)
                    .align(CenterHorizontally),
                avatar = "",
                placeholder = R.drawable.empty_avatar
            )
            Spacer(modifier = Modifier.height(16.dp))

            CommonTextField(label = "Никнейм", value = uiState.userData.nickname) {}
            CommonTextField(label = "Номер телефона", value = uiState.userData.phoneNumber) {}
            CommonTextField(
                label = "Город",
                value = uiState.userData.city,
                enabled = uiState.editEnabled,
                onValueChanged = onCityChange
            )
            CommonTextField(
                label = "Дата рождения",
                value = uiState.userData.birthDate,
                enabled = uiState.editEnabled,
                onValueChanged = onBirthDateChange
            )
            CommonTextField(
                label = "Знак зодиака",
                value = uiState.userData.zodiacSign,
                enabled = uiState.editEnabled,
                onValueChanged = onZodiacSignChange
            )
            CommonTextField(
                label = "О себе",
                value = uiState.userData.aboutMe,
                enabled = uiState.editEnabled,
                onValueChanged = onAboutMeChange
            )
        }
        if (uiState.isLoading) {
            CommonLoadingView()
        }
        if (uiState.editEnabled) {
            CommonButton(buttonText = "Сохранить") { onSaveChanges() }
        }
    }
}