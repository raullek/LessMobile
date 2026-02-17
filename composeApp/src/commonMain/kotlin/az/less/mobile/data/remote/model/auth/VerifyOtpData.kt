package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Verify OTP data from API response
 * POST /api/v1/auth/verify-otp
 */
@Serializable
data class VerifyOtpData(
    val message: String,
    val verified: Boolean
)
