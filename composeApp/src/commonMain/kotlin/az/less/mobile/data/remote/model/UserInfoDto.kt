package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * User info response from API
 * GET /api/v1/user/me
 */
@Serializable
data class UserInfoDto(
    val id: String,
    val name: String,
    val avatarUrl: String? = null
)