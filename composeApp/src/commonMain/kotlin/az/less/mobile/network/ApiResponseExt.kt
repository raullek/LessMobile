package az.less.mobile.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

/**
 * Extension function to wrap HttpResponse into NetworkResult
 * Handles success/error parsing automatically
 */
suspend inline fun <reified T> HttpResponse.toNetworkResult(): NetworkResult<T> {
    return try {
        if (status.isSuccess()) {
            val apiResponse = body<ApiResponse<T>>()
            if (apiResponse.isSuccess && apiResponse.data != null) {
                NetworkResult.Success(
                    data = apiResponse.data,
                    message = apiResponse.message,
                    meta = apiResponse.meta
                )
            } else {
                NetworkResult.Error(
                    ApiError(
                        message = apiResponse.message,
                        errors = apiResponse.errors,
                        meta = apiResponse.meta
                    )
                )
            }
        } else {
            val errorResponse = body<ApiError>()
            NetworkResult.Error(errorResponse)
        }
    } catch (e: Exception) {
        NetworkResult.Error(
            ApiError(message = e.message ?: "Unknown error")
        )
    }
}

/**
 * Safe API call wrapper that catches exceptions
 */
suspend inline fun <reified T> safeApiCall(
    call: () -> HttpResponse
): NetworkResult<T> {
    return try {
        call().toNetworkResult()
    } catch (e: Exception) {
        NetworkResult.Error(
            ApiError(message = e.message ?: "Network error")
        )
    }
}
