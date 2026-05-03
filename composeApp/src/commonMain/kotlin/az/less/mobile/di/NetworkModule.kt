package az.less.mobile.di

import az.less.mobile.BuildVariant
import az.less.mobile.currentBuildVariant
import az.less.mobile.data.remote.model.auth.RefreshTokenData
import az.less.mobile.data.remote.model.auth.RefreshTokenRequest
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.network.ApiResponse
import az.less.mobile.utils.deviceLocaleHeader
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private val AUTH_ENDPOINTS = listOf(
    "v1/auth/email-login",
    "v1/auth/verify-otp",
    "v1/auth/resend-otp",
    "v1/auth/login",
    "v1/auth/refresh-token",
)

val networkModule = module {
    single {
        val sessionLocalRepository: SessionLocalRepository = get()

        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            if (currentBuildVariant() == BuildVariant.DEVELOPMENT) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("HTTP: $message")
                        }
                    }
                    level = LogLevel.ALL
                }
            }
            install(SSE)
            install(Auth) {
                bearer {
                    loadTokens {
                        val access = sessionLocalRepository.getAccessToken()
                        val refresh = sessionLocalRepository.getRefreshToken()
                        if (access != null && refresh != null) {
                            BearerTokens(access, refresh)
                        } else {
                            null
                        }
                    }
                    refreshTokens {
                        val refresh = oldTokens?.refreshToken
                            ?: sessionLocalRepository.getRefreshToken()

                        if (refresh == null) {
                            sessionLocalRepository.clearSession()
                            return@refreshTokens null
                        }

                        try {
                            val response = client.post("v1/auth/refresh-token") {
                                markAsRefreshTokenRequest()
                                setBody(RefreshTokenRequest(refreshToken = refresh))
                            }

                            if (response.status.isSuccess()) {
                                val apiResponse = response.body<ApiResponse<RefreshTokenData>>()
                                if (apiResponse.isSuccess && apiResponse.data != null) {
                                    val data = apiResponse.data
                                    sessionLocalRepository.updateTokens(
                                        data.accessToken,
                                        data.refreshToken
                                    )
                                    BearerTokens(data.accessToken, data.refreshToken)
                                } else {
                                    sessionLocalRepository.clearSession()
                                    null
                                }
                            } else {
                                sessionLocalRepository.clearSession()
                                null
                            }
                        } catch (e: Exception) {
                            sessionLocalRepository.clearSession()
                            null
                        }
                    }
                    sendWithoutRequest { request ->
                        val url = request.url.buildString()
                        AUTH_ENDPOINTS.none { url.contains(it) }
                    }
                }
            }
            defaultRequest {
                url("https://axshambazari.com/api/")
                contentType(ContentType.Application.Json)
                headers.append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                headers.append("x-locale", deviceLocaleHeader())
                if (currentBuildVariant() == BuildVariant.DEVELOPMENT) {
                    headers.append("X-Data-Env", "test")
                    headers.append("X-Test-Db-Key", "axshamlar")
                }
            }
        }
    }
}
