package az.less.mobile.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VenueMerchantDto(
    @SerialName("_id") val id: String? = null,
    val venueId: String? = null,
    val userId: String? = null,
    val user: VenueMerchantUserDto? = null,
    val createdBy: String? = null,
    val isActive: Boolean? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class VenueMerchantUserDto(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val avatar: String? = null,
    val roles: List<String> = emptyList()
)

@Serializable
data class AddVenueMerchantRequest(
    val userIdentifier: String,
    val name: String,
    val email: String,
    val phone: String,
    val venueId: String
)

