package az.less.mobile.data.repository

import az.less.mobile.data.datasource.AuthDataSource
import az.less.mobile.data.remote.model.auth.EmailLoginData
import az.less.mobile.data.remote.model.auth.ResendOtpData
import az.less.mobile.data.remote.model.auth.VerifyOtpData
import az.less.mobile.network.NetworkResult
import az.less.mobile.domain.repository.AuthorizationRepository

class AuthorizationRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthorizationRepository {

    override suspend fun emailLogin(email: String): NetworkResult<EmailLoginData> {
        return authDataSource.emailLogin(email)
    }

    override suspend fun verifyOtp(email: String, otp: String): NetworkResult<VerifyOtpData> {
        return authDataSource.verifyOtp(email, otp)
    }

    override suspend fun resendOtp(email: String): NetworkResult<ResendOtpData> {
        return authDataSource.resendOtp(email)
    }
}
