package com.chatapp.presentation.authorization

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.chatapp.presentation.components.CommonButton
import com.chatapp.presentation.components.CommonLoadingView
import com.chatapp.presentation.components.CommonOutlinedTextField
import com.chatapp.presentation.components.CountryPicker
import com.chatapp.presentation.model.CountryDataUIModel
import com.chatapp.presentation.navigation.AuthorizationScreen
import com.chatapp.presentation.navigation.ChatsScreen
import com.chatapp.presentation.navigation.RegistrationScreen
import com.chatapp.ui.theme.LightNavy
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy
import com.chatapp.ui.theme.Typography

@Composable
fun AuthorizationScreen(
    navController: NavController,
    viewModel: AuthorizationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.countryData) {
        viewModel.sendUIEvent(AuthorizationUIEvent.CalculateDefaultRegion(context = context))
    }

    LaunchedEffect(uiState.navigation) {
        uiState.navigation?.let {screen ->
            when (screen) {
                ChatsScreen -> navController.navigate(screen) {popUpTo(AuthorizationScreen) {inclusive = true} }
                is RegistrationScreen -> navController.navigate(screen)
            }
            viewModel.sendUIEvent(AuthorizationUIEvent.OnNavigate)
        }
    }

    AuthorizationScreenContent(
        uiState = uiState,
        onSendPhoneNumber = { viewModel.sendUIEvent(AuthorizationUIEvent.SendPhoneNumber) },
        onCountrySelected = { country ->
            viewModel.sendUIEvent(AuthorizationUIEvent.OnCountryChanged(newCountry = country))
        },
        onPhoneNumberChanged = { phoneNumber ->
            viewModel.sendUIEvent(AuthorizationUIEvent.OnPhoneChanged(phone = phoneNumber))
        },
        onTextValueChanged = { confirmationCode ->
            viewModel.sendUIEvent(AuthorizationUIEvent.OnConfirmationCodeChanged(code = confirmationCode))
        },
        onSendConfirmationCode = { viewModel.sendUIEvent(AuthorizationUIEvent.SendConfirmationCode) }
    )
}

@Composable
fun AuthorizationScreenContent(
    uiState: AuthorizationUIState,
    onSendPhoneNumber: () -> Unit,
    onSendConfirmationCode: () -> Unit,
    onCountrySelected: (CountryDataUIModel) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onTextValueChanged: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LightNavy
    ) {
        Card(
            colors = CardColors(
                containerColor = Navy,
                contentColor = Color.Black,
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
                text = "Авторизация",
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.titleLarge,
                color = LightYellow
            )
            CountryPicker(
                countries = uiState.countryData,
                selectedCountry = uiState.selectedCountry,
                phoneNumber = uiState.phoneNumber,
                onCountrySelected = onCountrySelected,
                isError = !uiState.isPhoneNumberValid,
                onPhoneNumberChanged = onPhoneNumberChanged
            )
            if (uiState.isConfirmationCode) {
                CommonOutlinedTextField(
                    textValue = uiState.confirmationCodeValue,
                    onTextValueChanged = onTextValueChanged,
                    label = "Код подтверждения",
                    isError = !uiState.isCodeValid,
                    supportingText = "Код введен не полностью"
                )
            }
            CommonButton(buttonText = "Далее") {
                if (uiState.isConfirmationCode) {
                    onSendConfirmationCode()
                } else {
                    onSendPhoneNumber()
                }
            }
        }
        if (uiState.isLoading) {
            CommonLoadingView()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorizationPreview() {
    AuthorizationScreenContent(
        uiState = AuthorizationUIState(
            phoneNumber = "",
            countryData = emptyList(),
            isLoading = false,
            selectedCountry = CountryDataUIModel(
                name = "Belarus",
                code = "BY",
                iso = "",
                flag = "",
                mask = "##-#####-##"
            ),
            isCodeValid = true,
            isPhoneNumberValid = true,
            isConfirmationCode = false,
            confirmationCodeValue = ""
        ), {}, {}, {}, {}, {}
    )
}

