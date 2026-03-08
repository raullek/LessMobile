package az.less.mobile.network

import kotlinx.serialization.Serializable

/**
 * Generic API response wrapper for all network responses
 * @param T The type of data contained in the response
 */
@Serializable
data class ApiResponse<T>(
    val status: String,
    val message: String,
    val errors: List<String> = emptyList(),
    val data: T? = null,
    val meta: ResponseMeta? = null
) {
    val isSuccess: Boolean
        get() = status == "success"

    val isError: Boolean
        get() = status == "error"
}

/**
 * Metadata included in API responses
 */
@Serializable
data class ResponseMeta(
    val requestId: String? = null,
    val path: String? = null,
    val method: String? = null,
    val timestamp: String? = null,
    val statusCode: Int? = null
)
