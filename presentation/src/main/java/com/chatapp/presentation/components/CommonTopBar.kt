package com.chatapp.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    navIcon: ImageVector,
    actionIcon: ImageVector? = null,
    text: String,
    onActionClick: () -> Unit,
    onNavIconClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = text) },
        navigationIcon = {
            IconButton(onClick = { onNavIconClick() }) {
                Icon(navIcon, contentDescription = null)
            }
        },
        actions = {
            actionIcon?.let {
                IconButton(onClick = { onActionClick() }) {
                    Icon(actionIcon, contentDescription = null)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Navy,
            titleContentColor = LightYellow,
            actionIconContentColor = LightYellow,
            navigationIconContentColor = LightYellow
        )
    )
}