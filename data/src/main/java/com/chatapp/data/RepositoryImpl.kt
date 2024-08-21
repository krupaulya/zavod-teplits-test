package com.chatapp.data

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.chatapp.core.ApiResult
import com.chatapp.core.toResultFlow
import com.chatapp.data.local.database.dao.ChatsDao
import com.chatapp.data.local.database.dao.CountryDao
import com.chatapp.data.local.database.mapper.mapToUserData
import com.chatapp.data.local.database.mapper.toChatsData
import com.chatapp.data.local.database.mapper.toChatsDomain
import com.chatapp.data.local.database.mapper.toData
import com.chatapp.data.local.database.mapper.toDomain
import com.chatapp.data.local.filemanager.FileManager
import com.chatapp.data.local.prefs.JwtTokenManager
import com.chatapp.data.local.prefs.UserManager
import com.chatapp.data.remote.mapper.mapToAuthCodeSuccessModel
import com.chatapp.data.remote.mapper.mapToAvatarsModel
import com.chatapp.data.remote.mapper.mapToCheckCodeSuccessModel
import com.chatapp.data.remote.mapper.mapToRegisterUserSuccessModel
import com.chatapp.data.remote.mapper.mapToUserModel
import com.chatapp.data.remote.mapper.toRequest
import com.chatapp.data.remote.service.AuthService
import com.chatapp.data.remote.service.NoAuthService
import com.chatapp.domain.Repository
import com.chatapp.domain.model.ChatsModel
import com.chatapp.domain.model.CountryModel
import com.chatapp.domain.model.auth.UpdateUserModel
import com.chatapp.domain.model.auth.UserModel
import com.chatapp.domain.model.noauth.AuthCodeSuccessModel
import com.chatapp.domain.model.noauth.CheckAuthCodeModel
import com.chatapp.domain.model.noauth.CheckAuthCodeSuccessModel
import com.chatapp.domain.model.noauth.PhoneNumberModel
import com.chatapp.domain.model.noauth.RegisterUserModel
import com.chatapp.domain.model.noauth.RegisterUserSuccessModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val noAuthService: NoAuthService,
    private val authService: AuthService,
    private val countryDao: CountryDao,
    private val chatsDao: ChatsDao,
    private val jwtTokenManager: JwtTokenManager,
    private val userManager: UserManager,
    private val fileManager: FileManager,
    @ApplicationContext private val context: Context
) : Repository {

    override suspend fun sendAuthCode(phoneNumber: PhoneNumberModel): Flow<ApiResult<AuthCodeSuccessModel>> {
        return toResultFlow(
            call = { noAuthService.sendAuthCode(phoneNumber.toRequest()) },
            mapper = ::mapToAuthCodeSuccessModel
        )
    }

    override suspend fun loadCountriesFromJson(): List<CountryModel> {
        return load(fileName = "countries.json", object : TypeToken<List<CountryModel>>() {})
    }

    override suspend fun getAllCountries(): List<CountryModel> {
        return withContext(Dispatchers.IO) {
            if (countryDao.getAllCountries().isEmpty()) {
                countryDao.insertAll(loadCountriesFromJson().toData())
            }
            countryDao.getAllCountries().toDomain()
        }
    }

    override suspend fun checkAuthCode(data: CheckAuthCodeModel): Flow<ApiResult<CheckAuthCodeSuccessModel>> {
        return toResultFlow(
            call = { noAuthService.checkAuthCode(data.toRequest()) },
            mapper = ::mapToCheckCodeSuccessModel
        )
    }

    override suspend fun registerUser(userData: RegisterUserModel): Flow<ApiResult<RegisterUserSuccessModel>> {
        return toResultFlow(
            call = { noAuthService.registerUser(userData.toRequest()) },
            mapper = ::mapToRegisterUserSuccessModel
        )
    }

    override suspend fun loadChatsFromJson(): List<ChatsModel> {
        return load("chats.json", object : TypeToken<List<ChatsModel>>() {})
    }

    override suspend fun getAllChats(): List<ChatsModel> {
        return withContext(Dispatchers.IO) {
            if (chatsDao.getAllChats().isEmpty()) {
                chatsDao.insertAll(loadChatsFromJson().toChatsData())
            }
            chatsDao.getAllChats().toChatsDomain()
        }
    }

    override suspend fun getCurrentUser(): Flow<ApiResult<UserModel>> {
        return toResultFlow(
            call = { authService.getCurrentUser() },
            mapper = ::mapToUserModel
        )
    }

    override suspend fun saveTokens(refreshToken: String, accessToken: String) {
        jwtTokenManager.saveRefreshJwt(refreshToken)
        jwtTokenManager.saveAccessJwt(accessToken)
    }

    override suspend fun insertUser(userModel: UserModel) {
        chatsDao.insertUser(user = mapToUserData(userModel))
    }

    override suspend fun getUser(): UserModel? {
        return com.chatapp.data.local.database.mapper.mapToUserModel(chatsDao.getUser(userManager.getUserId() ?: 0))
    }

    override suspend fun saveUserId(userId: Int) {
        userManager.saveUserId(userId)
    }

    override suspend fun updateUser(updateUserModel: UpdateUserModel): Flow<ApiResult<UserModel.Avatars>> {
        return toResultFlow(
            call = { authService.updateCurrentUser(updateUser = updateUserModel.toRequest()) },
            mapper = ::mapToAvatarsModel
        )
    }

    override suspend fun localUpdateUser(
        birthday: String?,
        city: String?,
        vk: String?,
        instagram: String?,
        status: String?,
        avatar: String?,
        aboutMe: String?,
        zodiacSign: String?
    ) {
        chatsDao.updateUser(
            id = userManager.getUserId() ?: 0,
            birthday, city, vk, instagram, status, avatar, aboutMe, zodiacSign
        )
    }

    override fun getBase64FromUri(contentResolver: ContentResolver, uri: Uri): String? {
        return fileManager.getBase64FromUri(contentResolver, uri)
    }

    override fun base64ToBitmap(base64String: String?): Bitmap? {
        return fileManager.base64ToBitmap(base64String)
    }

    private fun <T> load(fileName: String, typeToken: TypeToken<List<T>>): List<T> {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val listType = typeToken.type
        return Gson().fromJson(jsonString, listType)
    }
}