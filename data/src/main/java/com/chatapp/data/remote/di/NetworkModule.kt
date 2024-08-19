package com.chatapp.data.remote.di


import com.chatapp.data.remote.interceptor.AccessTokenInterceptor
import com.chatapp.data.remote.AuthAuthenticator
import com.chatapp.data.remote.interceptor.RefreshTokenInterceptor
import com.chatapp.data.remote.service.AuthService
import com.chatapp.data.remote.service.NoAuthService
import com.chatapp.data.remote.service.RefreshTokenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

private const val BASE_URL: String = "https://plannerok.ru/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAccessOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        authAuthenticator: AuthAuthenticator
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .authenticator(authAuthenticator)
            .addInterceptor(accessTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @TokenRefreshClient
    fun provideRefreshOkHttpClient(
        refreshTokenInterceptor: RefreshTokenInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(refreshTokenInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @PublicClient
    fun provideUnauthenticatedOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AuthenticatedClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TokenRefreshClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class PublicClient

    @Provides
    @Singleton
    fun provideAuthenticationApi(@AuthenticatedClient okHttpClient: OkHttpClient): AuthService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideRefreshTokenApi(@TokenRefreshClient okHttpClient: OkHttpClient): RefreshTokenService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RefreshTokenService::class.java)
    }

    @Provides
    @Singleton
    fun provideNoAuthenticationApi(@PublicClient okHttpClient: OkHttpClient): NoAuthService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(NoAuthService::class.java)
    }
}