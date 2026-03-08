package az.less.mobile.data.remote.model.account

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val birthDay: String? = null
)

@Serializable
data class UpdateUserData(
    val id: String,
    val email: String,
    val name: String,
    val roles: List<String> = emptyList(),
    val status: String = "",
    val phone: String? = null,
    val avatar: String? = null,
    val gender: String? = null,
    val birthDay: String? = null
)
