package az.less.mobile.data.model

import az.less.mobile.domain.model.auth.User
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val roles: List<String>,
    val status: String,
    val avatarUrl: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val birthDay: String? = null
) {
    fun toUser(): User = User(
        id = id,
        name = name,
        email = email,
        roles = roles,
        status = status,
        avatarUrl = avatarUrl,
        phone = phone,
        gender = gender,
        birthDay = birthDay
    )
}
