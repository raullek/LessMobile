package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

/**
 * Email login request body
 * POST /api/v1/auth/email-login
 */
@Serializable
data class EmailLoginRequest(
    val email: String
)
