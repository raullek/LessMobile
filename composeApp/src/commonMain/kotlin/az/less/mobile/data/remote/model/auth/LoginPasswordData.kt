package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Request body for POST /api/v1/auth/login
 */
@Serializable
data class LoginPasswordRequest(
    val email: String,
    val password: String
)

/**
 * Response data from POST /api/v1/auth/login
 * Contains tokens and user info for immediate session creation.
 */
@Serializable
data class LoginPasswordData(
    val accessToken: String,
    val refreshToken: String,
    val user: VerifyOtpUser
)
