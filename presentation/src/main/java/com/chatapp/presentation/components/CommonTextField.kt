package com.chatapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy
import com.chatapp.ui.theme.Typography

@Composable
fun CommonTextField(
    value: String?,
    label: String,
    enabled: Boolean = false,
    onValueChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = value.orEmpty(),
            onValueChange = { newValue ->
                onValueChanged(newValue)
            },
            label = { Text(label, style = Typography.bodySmall, modifier = Modifier.padding(bottom = 4.dp)) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = Typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                focusedTextColor = LightYellow,
                unfocusedLabelColor = LightYellow,
                focusedLabelColor = LightYellow,
                unfocusedTextColor = LightYellow,
                focusedContainerColor = Navy,
                unfocusedContainerColor = Navy,
                disabledContainerColor = Navy,
                disabledTextColor = Color.LightGray,
                errorContainerColor = Color.Transparent,
                cursorColor = LightYellow,
                disabledLabelColor = Color.LightGray,
            ),
            enabled = enabled
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = Color.Gray
        )
    }
}