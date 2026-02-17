package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Email login data from API response
 * POST /api/v1/auth/email-login
 * This is the "data" field inside ApiResponse
 */
@Serializable
data class EmailLoginData(
    val email: String,
    val message: String? = null,
    val maskedEmail: String? = null,
    val displayEmail: String? = null
)
