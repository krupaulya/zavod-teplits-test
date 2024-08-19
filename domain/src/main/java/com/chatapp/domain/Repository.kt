package com.chatapp.domain

import com.chatapp.core.ApiResult
import com.chatapp.domain.model.ChatsModel
import com.chatapp.domain.model.noauth.PhoneNumberModel
import com.chatapp.domain.model.noauth.AuthCodeSuccessModel
import com.chatapp.domain.model.noauth.CheckAuthCodeModel
import com.chatapp.domain.model.noauth.CheckAuthCodeSuccessModel
import com.chatapp.domain.model.CountryModel
import com.chatapp.domain.model.auth.UpdateUserModel
import com.chatapp.domain.model.auth.UserModel
import com.chatapp.domain.model.noauth.RegisterUserModel
import com.chatapp.domain.model.noauth.RegisterUserSuccessModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun sendAuthCode(phoneNumber: PhoneNumberModel): Flow<ApiResult<AuthCodeSuccessModel>>

    suspend fun loadCountriesFromJson(): List<CountryModel>

    suspend fun getAllCountries(): List<CountryModel>

    suspend fun checkAuthCode(data: CheckAuthCodeModel): Flow<ApiResult<CheckAuthCodeSuccessModel>>

    suspend fun registerUser(userData: RegisterUserModel): Flow<ApiResult<RegisterUserSuccessModel>>

    suspend fun loadChatsFromJson(): List<ChatsModel>

    suspend fun getAllChats(): List<ChatsModel>

    suspend fun getCurrentUser(): Flow<ApiResult<UserModel>>

    suspend fun saveTokens(refreshToken: String, accessToken: String)

    suspend fun insertUser(userModel: UserModel)

    suspend fun getUser(): UserModel?

    suspend fun saveUserId(userId: Int)

    suspend fun updateUser(updateUserModel: UpdateUserModel): Flow<ApiResult<UserModel.Avatars>>

    suspend fun localUpdateUser(
        birthday: String?,
        city: String?,
        vk: String?,
        instagram: String?,
        status: String?,
        avatar: String?,
        aboutMe: String?
    )
}