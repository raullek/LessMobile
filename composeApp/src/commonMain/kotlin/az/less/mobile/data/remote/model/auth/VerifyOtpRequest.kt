package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Verify OTP request body
 * POST /api/v1/auth/verify-otp
 */
@Serializable
data class VerifyOtpRequest(
    val email: String,
    val otp: String
)
