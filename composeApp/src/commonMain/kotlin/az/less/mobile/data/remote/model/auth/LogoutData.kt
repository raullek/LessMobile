package az.less.mobile.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LogoutData(
    val message: String? = null
)
