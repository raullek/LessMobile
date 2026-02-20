package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenData(
    val accessToken: String,
    val refreshToken: String
)
