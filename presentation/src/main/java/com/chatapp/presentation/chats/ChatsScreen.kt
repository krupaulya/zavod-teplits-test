package com.chatapp.presentation.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.chatapp.presentation.components.CommonTopBar
import com.chatapp.presentation.components.UserImage
import com.chatapp.presentation.model.ChatsDataUIModel
import com.chatapp.presentation.navigation.AuthorizationScreen
import com.chatapp.presentation.navigation.ChatScreen
import com.chatapp.presentation.navigation.ChatsScreen
import com.chatapp.presentation.navigation.ProfileScreen
import com.chatapp.ui.R
import com.chatapp.ui.theme.LightNavy
import com.chatapp.ui.theme.LightYellow
import com.chatapp.ui.theme.Navy
import com.chatapp.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun ChatsScreen(
    navController: NavController,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatsScreenContent(
        chats = uiState.chats,
        onChatItemClick = { data ->
            navController.navigate(ChatScreen(chatDataId = data))
        },
        navController = navController
    )
}

@Composable
fun ChatsScreenContent(
    chats: List<ChatsDataUIModel>,
    onChatItemClick: (Int) -> Unit,
    navController: NavController
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController)
        }
    ) {
        Scaffold(
            topBar = {
                CommonTopBar(navIcon = Icons.Default.Menu, text = "Чаты", onActionClick = {}) { scope.launch { drawerState.open() } }
            },
            containerColor = Navy,
            contentColor = Color.White
        ) { paddingValues ->
            ChatList(
                chats = chats,
                modifier = Modifier.padding(paddingValues),
                onChatItemClick = onChatItemClick
            )
        }
    }
}

@Composable
private fun ChatList(
    chats: List<ChatsDataUIModel>,
    modifier: Modifier = Modifier,
    onChatItemClick: (Int) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(chats.size) { index ->
            ChatItem(chats[index], onChatItemClick = onChatItemClick)
        }
    }
}

@Composable
private fun ChatItem(chat: ChatsDataUIModel, onChatItemClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChatItemClick(chat.id) }
            .background(color = Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserImage(
            modifier = Modifier.size(48.dp),
            avatar = "",
            placeholder = R.drawable.empty_avatar
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = chat.writer,
                style = Typography.labelLarge,
                color = LightYellow
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chat.texts.last().message,
                modifier = Modifier.padding(bottom = 8.dp),
                style = Typography.labelMedium
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color.Gray
            )
        }
    }
}

@Composable
fun DrawerContent(navController: NavController) {
    ModalDrawerSheet(drawerContainerColor = Navy) {
        DrawerHeader()
        Spacer(modifier = Modifier.padding(6.dp))
        NavigationItem(icon = Icons.Default.Person, name = "Профиль") { navController.navigate(ProfileScreen) }
        Spacer(modifier = Modifier.padding(2.dp))
        NavigationItem(icon = Icons.Default.Home, name = "Дом") {}
        Spacer(modifier = Modifier.padding(2.dp))
        NavigationItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            name = "Выйти"
        ) { navController.navigate(AuthorizationScreen) { popUpTo(ChatsScreen) { inclusive = true } } }
    }
}

@Composable
private fun DrawerHeader(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(LightNavy)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        UserImage(
            modifier = Modifier.size(70.dp), avatar = "",
            placeholder = R.drawable.empty_avatar
        )
        Spacer(modifier = Modifier.padding(6.dp))
        Text(
            text = "Shpuliya",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = LightYellow
        )
    }
}

@Composable
private fun NavigationItem(
    icon: ImageVector,
    name: String,
    onClick: () -> Unit,
) {
    val colors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = Navy,
        unselectedContainerColor = Navy,
        selectedTextColor = LightYellow,
        unselectedTextColor = LightYellow,
        selectedIconColor = LightYellow,
        unselectedIconColor = LightYellow,
        selectedBadgeColor = LightYellow,
        unselectedBadgeColor = LightYellow
    )
    NavigationDrawerItem(
        label = { Text(text = name, style = MaterialTheme.typography.labelSmall) },
        selected = false,
        onClick = { onClick() },
        icon = { Icon(imageVector = icon, contentDescription = null) },
        colors = colors
    )
}

@Preview(showBackground = true)
@Composable
fun ChatsScreenPreview() {
    val context = LocalContext.current
    ChatsScreenContent(chats = emptyList(), {}, navController = NavController(context))
}

