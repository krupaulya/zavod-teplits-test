package com.chatapp.presentation.navigation

import kotlinx.serialization.Serializable


interface Navigation

@Serializable
object AuthorizationScreen: Navigation

@Serializable
data class RegistrationScreen(val phoneNumber: String): Navigation

@Serializable
object ChatsScreen: Navigation

@Serializable
data class ChatScreen(val chatDataId: Int): Navigation

@Serializable
data object ProfileScreen: Navigation
