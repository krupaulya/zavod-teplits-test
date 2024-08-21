package com.chatapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDatePicker(
    onShowDialogChange: () -> Unit,
    onDateSelected: (Long?) -> Unit,
) {
    val dateState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = { onShowDialogChange() },
        modifier = Modifier.background(color = Navy),
        confirmButton = {
            Button(
                onClick = {
                    onShowDialogChange()
                    onDateSelected(dateState.selectedDateMillis)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(text = "OK", color = LightYellow)
            }
        },
        dismissButton = {
            Button(
                onClick = { onShowDialogChange() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(text = "Отмена", color = LightYellow)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Navy
        )
    ) {
        DatePicker(
            state = dateState,
            modifier = Modifier.background(color = Navy),
            colors = DatePickerDefaults.colors(
                todayDateBorderColor = LightYellow,
                navigationContentColor = LightYellow,
                selectedDayContentColor = Navy,
                containerColor = Navy,
                selectedYearContentColor = Navy,
                titleContentColor = LightYellow,
                headlineContentColor = LightYellow,
                weekdayContentColor = LightYellow,
                yearContentColor = LightYellow,
                dividerColor = LightYellow,
                selectedDayContainerColor = LightYellow,
                selectedYearContainerColor = LightYellow,
                currentYearContentColor = LightYellow,
                todayContentColor = LightYellow,
                dayContentColor = LightYellow,
                dateTextFieldColors = OutlinedTextFieldDefaults.colors(
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
                )
            )
        )
    }
}