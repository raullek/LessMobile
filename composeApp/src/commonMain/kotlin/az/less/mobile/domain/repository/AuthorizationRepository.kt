package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.auth.EmailLoginData
import az.less.mobile.data.remote.model.auth.LoginPasswordData
import az.less.mobile.data.remote.model.auth.LogoutData
import az.less.mobile.data.remote.model.auth.RefreshTokenData
import az.less.mobile.data.remote.model.auth.ResendOtpData
import az.less.mobile.data.remote.model.auth.VerifyOtpData
import az.less.mobile.network.NetworkResult

interface AuthorizationRepository {
    suspend fun emailLogin(email: String): NetworkResult<EmailLoginData>
    suspend fun verifyOtp(email: String, otp: String): NetworkResult<VerifyOtpData>
    suspend fun loginWithPassword(email: String, password: String): NetworkResult<LoginPasswordData>
    suspend fun resendOtp(email: String): NetworkResult<ResendOtpData>
    suspend fun refreshToken(refreshToken: String): NetworkResult<RefreshTokenData>
    suspend fun logout(refreshToken: String): NetworkResult<LogoutData>
}
