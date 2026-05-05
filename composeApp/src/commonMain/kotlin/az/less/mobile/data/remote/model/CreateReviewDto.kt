package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewRequest(
    val orderId: String,
    val rating: Int,
    val comment: String
)

@Serializable
data class CreateReviewResponseDto(
    val id: String? = null,
    val rating: Int? = null,
    val comment: String? = null,
    val createdAt: String? = null
)
