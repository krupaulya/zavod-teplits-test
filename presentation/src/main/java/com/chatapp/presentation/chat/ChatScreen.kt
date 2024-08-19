package com.chatapp.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.chatapp.presentation.components.CommonTopBar
import com.chatapp.presentation.model.ChatsDataUIModel
import com.chatapp.ui.theme.LightBlue
import com.chatapp.ui.theme.LightNavy
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy
import com.chatapp.ui.theme.Typography

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreenContent(
        chat = uiState.chat,
        navController = navController
    )
}

@Composable
fun ChatScreenContent(
    navController: NavController,
    chat: ChatsDataUIModel
) {
    Scaffold(
        topBar = {
            CommonTopBar(navIcon = Icons.AutoMirrored.Filled.ArrowBack, text = chat.writer, onActionClick = {}) { navController.navigateUp() }
        },
        containerColor = Navy,
        contentColor = Color.White
    ) { paddingValues ->
        ChatDetailContent(
            chat = chat,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ChatDetailContent(
    chat: ChatsDataUIModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Navy)
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(chat.texts.size) { index ->
                val message = chat.texts[chat.texts.size - 1 - index]
                MessageItem(
                    message = message,
                    isSentByUser = index % 2 == 0
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        MessageInput()
    }
}

@Composable
fun MessageItem(
    message: ChatsDataUIModel.Message,
    isSentByUser: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isSentByUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSentByUser) LightBlue else LightNavy,
            ),
            modifier = Modifier
                .widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = message.message, style = Typography.labelLarge, color = if (isSentByUser) LightNavy else LightYellow)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = message.time, style = Typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    onSendMessage: (String) -> Unit = {}
) {
    var message by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(LightNavy, RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        TextField(
            value = message,
            onValueChange = { message = it },
            placeholder = { Text(text = "Введите сообщение", color = Color.Gray) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = LightYellow
            ),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            if (message.isNotEmpty()) {
                onSendMessage(message)
                message = ""
            }
        }) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = LightYellow)
        }
    }
}