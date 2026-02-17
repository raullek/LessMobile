package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Resend OTP request body
 * POST /api/v1/auth/resend-otp
 */
@Serializable
data class ResendOtpRequest(
    val email: String
)
