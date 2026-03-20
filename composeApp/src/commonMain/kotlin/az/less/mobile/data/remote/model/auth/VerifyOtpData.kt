package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Verify OTP data from API response
 * POST /api/v1/auth/verify-otp
 */
@Serializable
data class VerifyOtpData(
    val accessToken: String,
    val refreshToken: String,
    val user: VerifyOtpUser,
    val isNewUser: Boolean,
    val message: String
)

@Serializable
data class VerifyOtpUser(
    val id: String,
    val email: String,
    val name: String,
    val phone: String? = null,
    val avatar: String? = null,
    val gender: String? = null,
    val birthDay: String? = null,
    val roles: List<String>,
    val status: String,
    val venue: VerifyOtpVenue? = null
)

@Serializable
data class VerifyOtpVenue(
    val id: String,
    val name: String,
    val businessName: String? = null,
    val businessAddress: String? = null,
    val rating: Double = 0.0,
    val totalReviews: Int = 0,
    val businessLogo: String? = null,
    val status: String? = null
)
