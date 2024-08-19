package com.chatapp.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.ui.theme.LightNavy
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Typography

@Composable
fun CommonButton(
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .height(52.dp),
        colors = ButtonColors(
            containerColor = LightNavy,
            contentColor = LightYellow,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.Gray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = buttonText, style = Typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun CommonButtonPreview() {
    CommonButton(
        buttonText = "Next",
        onClick = {}
    )
}