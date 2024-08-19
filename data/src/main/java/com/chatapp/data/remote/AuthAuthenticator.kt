package com.chatapp.data.remote

import com.chatapp.data.local.prefs.JwtTokenManager
import com.chatapp.data.remote.model.refresh.RefreshTokenRequest
import com.chatapp.data.remote.service.RefreshTokenService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: JwtTokenManager,
    private val refreshTokenService: RefreshTokenService
) : Authenticator {

    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val currentToken = runBlocking {
            tokenManager.getAccessJwt()
        }
        synchronized(this) {
            val updatedToken = runBlocking {
                tokenManager.getAccessJwt()
            }
            val token = if (currentToken != updatedToken) updatedToken else {
                val newSessionResponse = runBlocking {
                    refreshTokenService.refreshToken(RefreshTokenRequest(refreshToken = tokenManager.getRefreshJwt().orEmpty()))
                }
                if (newSessionResponse.isSuccessful) {
                    newSessionResponse.body()?.let { body ->
                        runBlocking {
                            body.accessToken?.let { tokenManager.saveAccessJwt(it) }
                            body.refreshToken?.let { tokenManager.saveRefreshJwt(it) }
                        }
                        body.accessToken
                    }
                } else null
            }
            return if (token != null) response.request.newBuilder()
                .header(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
                .build() else null
        }
    }
}