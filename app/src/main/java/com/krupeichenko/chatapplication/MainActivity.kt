package com.krupeichenko.chatapplication

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chatapp.presentation.navigation.AuthorizationScreen
import com.chatapp.presentation.navigation.ChatsScreen
import com.chatapp.presentation.navigation.RegistrationScreen
import com.chatapp.presentation.authorization.AuthorizationScreen
import com.chatapp.presentation.chat.ChatScreen
import com.chatapp.presentation.chats.ChatsScreen
import com.chatapp.presentation.navigation.ChatScreen
import com.chatapp.presentation.navigation.ProfileScreen
import com.chatapp.presentation.profile.ProfileScreen
import com.chatapp.presentation.registration.RegistrationScreen
import com.chatapp.ui.theme.ChatApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
        setContent {
            ChatApplicationTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AuthorizationScreen
                ) {
                    composable<AuthorizationScreen> {
                        AuthorizationScreen(navController)
                    }
                    composable<RegistrationScreen> {
                        val args = it.toRoute<RegistrationScreen>()
                        RegistrationScreen(navController, args.phoneNumber)
                    }
                    composable<ChatsScreen> {
                        ChatsScreen(navController = navController)
                    }
                    composable<ChatScreen> {
                        ChatScreen(navController = navController)
                    }
                    composable<ProfileScreen> {
                        ProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}