package az.less.mobile.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VenueMerchantDto(
    @SerialName("_id") val id: String,
    val venueId: String,
    val userId: VenueMerchantUserDto,
    val createdBy: String? = null,
    val isActive: Boolean = true,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class VenueMerchantUserDto(
    val id: String,
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

@Serializable
data class AddVenueMerchantResponseDto(
    val merchant: AddedMerchantDto,
    val isNewUser: Boolean = false,
    val message: String? = null
)

@Serializable
data class AddedMerchantDto(
    @SerialName("_id") val id: String,
    val userId: String,
    val venueId: String,
    val createdBy: String? = null,
    val isActive: Boolean = true,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
