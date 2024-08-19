package com.chatapp.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy
import com.chatapp.ui.theme.Typography

@Composable
fun CommonOutlinedTextField(
    modifier: Modifier = Modifier,
    textValue: String,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Number,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation? = null,
    onTextValueChanged: (String) -> Unit,
    supportingText: String? = null,
    isError: Boolean = false,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = textValue,
        onValueChange = { newValue -> onTextValueChanged(newValue) },
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        leadingIcon = {
            if (leadingIcon != null) {
                leadingIcon()
            }
        },
        placeholder = { Text(text = placeholder.orEmpty()) },
        label = {
            Text(text = label, style = Typography.bodySmall)
        },
        supportingText = {
            Row {
                Text(if (isError) supportingText.orEmpty() else "", Modifier.clearAndSetSemantics {}, style = Typography.labelSmall)
            }
        },
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        textStyle = Typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = LightYellow,
            unfocusedTextColor = LightYellow,
            focusedContainerColor = Navy,
            unfocusedContainerColor = Navy,
            disabledContainerColor = Navy,
            disabledTextColor = Color.LightGray,
            disabledLabelColor = Color.LightGray,
            disabledBorderColor = Color.LightGray,
            errorContainerColor = Color.Transparent,
            cursorColor = Navy,
            focusedBorderColor = LightYellow,
            unfocusedBorderColor = LightYellow,
            focusedLabelColor = LightYellow,
            unfocusedLabelColor = LightYellow,
            errorTextColor = LightYellow
        ),
        enabled = enabled
    )
}