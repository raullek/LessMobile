package az.less.mobile.network

import kotlinx.serialization.Serializable

/**
 * API error response model
 */
@Serializable
data class ApiError(
    val status: String = "error",
    val message: String,
    val errors: List<String> = emptyList(),
    val meta: ResponseMeta? = null
)
