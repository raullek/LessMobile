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
            val errorResponse = try {
                body<ApiError>()
            } catch (_: Exception) {
                ApiError(message = "Unknown error")
            }
            NetworkResult.Error(
                errorResponse.copy(
                    meta = (errorResponse.meta ?: ResponseMeta()).copy(
                        statusCode = status.value
                    )
                )
            )
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

/**
 * Safe API call that only validates success/error status,
 * ignoring the data payload. Use for endpoints where response data is not needed.
 */
suspend fun safeApiCallUnit(
    call: suspend () -> HttpResponse
): NetworkResult<Unit> {
    return try {
        val response = call()
        if (response.status.isSuccess()) {
            val apiResponse = response.body<ApiResponse<kotlinx.serialization.json.JsonElement>>()
            if (apiResponse.isSuccess) {
                NetworkResult.Success(
                    data = Unit,
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
            val errorResponse = try {
                response.body<ApiError>()
            } catch (_: Exception) {
                ApiError(message = "Unknown error")
            }
            NetworkResult.Error(
                errorResponse.copy(
                    meta = (errorResponse.meta ?: ResponseMeta()).copy(
                        statusCode = response.status.value
                    )
                )
            )
        }
    } catch (e: Exception) {
        NetworkResult.Error(
            ApiError(message = e.message ?: "Network error")
        )
    }
}
