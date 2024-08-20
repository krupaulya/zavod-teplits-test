package com.chatapp.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter

@Composable
fun UserImage(
    modifier: Modifier,
    avatar: Bitmap? = null,
    placeholder: Int,
    clickable: Boolean = false,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(enabled = clickable) { onClick() }
    ) {
        if (avatar != null) {
            Image(
                painter = rememberAsyncImagePainter(model = avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painterResource(id = placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

