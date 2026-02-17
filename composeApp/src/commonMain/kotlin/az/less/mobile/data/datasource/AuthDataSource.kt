package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.auth.EmailLoginData
import az.less.mobile.data.remote.model.auth.EmailLoginRequest
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
}
