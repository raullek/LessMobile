package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Resend OTP data from API response
 * POST /api/v1/auth/resend-otp
 */
@Serializable
data class ResendOtpData(
    val message: String,
    val email: String
)
