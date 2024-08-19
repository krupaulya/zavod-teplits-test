package com.chatapp.presentation.registration

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.chatapp.presentation.components.CommonButton
import com.chatapp.presentation.components.CommonLoadingView
import com.chatapp.presentation.components.CommonOutlinedTextField
import com.chatapp.ui.theme.LightNavy
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy
import com.chatapp.ui.theme.Typography

@Composable
fun RegistrationScreen(
    navController: NavController,
    arguments: String,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        uiState.navigation?.let {
            navController.navigate(it)
            viewModel.sendUIEvent(RegistrationUIEvent.OnNavigate)
        }
    }

    LaunchedEffect(arguments) {
        viewModel.sendUIEvent(RegistrationUIEvent.OnAddArguments(arguments = arguments))
    }

    RegistrationScreenContent(
        uiState = uiState,
        onNameValueChanged = { viewModel.sendUIEvent(RegistrationUIEvent.OnNameChanged(name = it)) },
        onUserNameValueChanged = { viewModel.sendUIEvent(RegistrationUIEvent.OnUserNameChanged(userName = it)) },
        onNextButtonClick = { viewModel.sendUIEvent(RegistrationUIEvent.RegisterUser) }
    )
}

@Composable
fun RegistrationScreenContent(
    uiState: RegistrationUIState,
    onNameValueChanged: (String) -> Unit,
    onUserNameValueChanged: (String) -> Unit,
    onNextButtonClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LightNavy
    ) {
        Card(
            colors = CardColors(
                containerColor = Navy,
                contentColor = LightYellow,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Navy
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = "Регистрация",
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.titleLarge,
                color = LightYellow
            )
            CommonOutlinedTextField(
                textValue = uiState.phoneNumber,
                enabled = false,
                onTextValueChanged = {},
                label = "Номер телефона"
            )
            CommonOutlinedTextField(
                textValue = uiState.name,
                keyboardType = KeyboardType.Text,
                onTextValueChanged = onNameValueChanged,
                label = "Введите имя"
            )
            CommonOutlinedTextField(
                textValue = uiState.userName,
                keyboardType = KeyboardType.Text,
                onTextValueChanged = onUserNameValueChanged,
                label = "Введите юзернэйм",
                isError = !uiState.isUserNameValid,
                supportingText = "Используйте латинские буквы, цифры, а также символы -_"
            )
            CommonButton(buttonText = "Далее") { onNextButtonClick() }
        }
        if (uiState.isLoading) {
            CommonLoadingView()
        }
    }
}