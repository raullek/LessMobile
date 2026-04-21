package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.auth.EmailLoginData
import az.less.mobile.data.remote.model.auth.EmailLoginRequest
import az.less.mobile.data.remote.model.auth.LoginPasswordData
import az.less.mobile.data.remote.model.auth.LoginPasswordRequest
import az.less.mobile.data.remote.model.auth.LogoutData
import az.less.mobile.data.remote.model.auth.RefreshTokenData
import az.less.mobile.data.remote.model.auth.RefreshTokenRequest
import az.less.mobile.data.remote.model.auth.ResendOtpData
import az.less.mobile.data.remote.model.auth.ResendOtpRequest
import az.less.mobile.data.remote.model.auth.VerifyOtpData
import az.less.mobile.data.remote.model.auth.VerifyOtpRequest
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthDataSource(
    private val httpClient: HttpClient
) {
    suspend fun emailLogin(email: String): NetworkResult<EmailLoginData> {
        return safeApiCall {
            httpClient.post("v1/auth/email-login") {
                setBody(EmailLoginRequest(email = email))
            }
        }
    }

    suspend fun verifyOtp(email: String, otp: String): NetworkResult<VerifyOtpData> {
        return safeApiCall {
            httpClient.post("v1/auth/verify-otp") {
                setBody(VerifyOtpRequest(email = email, otp = otp))
            }
        }
    }

    suspend fun resendOtp(email: String): NetworkResult<ResendOtpData> {
        return safeApiCall {
            httpClient.post("v1/auth/resend-otp") {
                setBody(ResendOtpRequest(email = email))
            }
        }
    }

    suspend fun loginWithPassword(email: String, password: String): NetworkResult<LoginPasswordData> {
        return safeApiCall {
            httpClient.post("v1/auth/login") {
                setBody(LoginPasswordRequest(email = email, password = password))
            }
        }
    }

    suspend fun refreshToken(refreshToken: String): NetworkResult<RefreshTokenData> {
        return safeApiCall {
            httpClient.post("v1/auth/refresh-token") {
                setBody(RefreshTokenRequest(refreshToken = refreshToken))
            }
        }
    }

    suspend fun logout(refreshToken: String): NetworkResult<LogoutData> {
        return safeApiCall {
            httpClient.post("v1/auth/logout") {
                setBody(RefreshTokenRequest(refreshToken = refreshToken))
            }
        }
    }
}
