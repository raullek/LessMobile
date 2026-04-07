package az.less.mobile.data.remote.model.account

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val birthDay: String? = null
)
