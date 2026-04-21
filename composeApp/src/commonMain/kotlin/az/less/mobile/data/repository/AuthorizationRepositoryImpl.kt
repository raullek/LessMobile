package az.less.mobile.data.repository

import az.less.mobile.data.datasource.AccountDataSource
import az.less.mobile.data.datasource.AuthDataSource
import az.less.mobile.data.remote.model.auth.EmailLoginData
import az.less.mobile.data.remote.model.auth.LoginPasswordData
import az.less.mobile.data.remote.model.auth.LogoutData
import az.less.mobile.data.remote.model.auth.RefreshTokenData
import az.less.mobile.data.remote.model.auth.ResendOtpData
import az.less.mobile.data.remote.model.auth.VerifyOtpData
import az.less.mobile.data.remote.model.mapper.toUser
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.network.NetworkResult

class AuthorizationRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val accountDataSource: AccountDataSource,
    private val sessionLocalRepository: SessionLocalRepository
) : AuthorizationRepository {

    override suspend fun emailLogin(email: String): NetworkResult<EmailLoginData> {
        return authDataSource.emailLogin(email)
    }

    /**
     * Verify OTP, save tokens, fetch user profile and save user.
     * If profile fetch fails, the whole operation fails and session is cleared.
     */
    override suspend fun verifyOtp(email: String, otp: String): NetworkResult<VerifyOtpData> {
        val otpResult = authDataSource.verifyOtp(email, otp)

        if (otpResult is NetworkResult.Error) return otpResult

        val otpData = (otpResult as NetworkResult.Success).data

        // Save tokens first so the bearer plugin picks them up for getProfile
        sessionLocalRepository.updateTokens(otpData.accessToken, otpData.refreshToken)

        // Fetch full user profile
        val profileResult = accountDataSource.getProfile()

        if (profileResult is NetworkResult.Error) {
            sessionLocalRepository.clearSession()
            return NetworkResult.Error(profileResult.error)
        }

        val profileData = (profileResult as NetworkResult.Success).data

        // Save full session (user + tokens)
        sessionLocalRepository.saveSession(
            user = profileData.toUser(),
            accessToken = otpData.accessToken,
            refreshToken = otpData.refreshToken
        )

        return otpResult
    }

    override suspend fun loginWithPassword(email: String, password: String): NetworkResult<LoginPasswordData> {
        val loginResult = authDataSource.loginWithPassword(email, password)

        if (loginResult is NetworkResult.Error) return loginResult

        val loginData = (loginResult as NetworkResult.Success).data

        // Save tokens first so the bearer plugin picks them up for getProfile
        sessionLocalRepository.updateTokens(loginData.accessToken, loginData.refreshToken)

        // Fetch full user profile
        val profileResult = accountDataSource.getProfile()

        if (profileResult is NetworkResult.Error) {
            sessionLocalRepository.clearSession()
            return NetworkResult.Error(profileResult.error)
        }

        val profileData = (profileResult as NetworkResult.Success).data

        // Save full session (user + tokens)
        sessionLocalRepository.saveSession(
            user = profileData.toUser(),
            accessToken = loginData.accessToken,
            refreshToken = loginData.refreshToken
        )

        return loginResult
    }

    override suspend fun resendOtp(email: String): NetworkResult<ResendOtpData> {
        return authDataSource.resendOtp(email)
    }

    override suspend fun refreshToken(refreshToken: String): NetworkResult<RefreshTokenData> {
        return authDataSource.refreshToken(refreshToken)
    }

    override suspend fun logout(refreshToken: String): NetworkResult<LogoutData> {
        return authDataSource.logout(refreshToken)
    }
}
