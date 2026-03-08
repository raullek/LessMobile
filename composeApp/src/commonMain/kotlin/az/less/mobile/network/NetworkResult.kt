package az.less.mobile.network

/**
 * Sealed class representing the result of a network operation
 * @param T The type of data on success
 */
sealed class NetworkResult<out T> {

    data class Success<T>(
        val data: T,
        val message: String,
        val meta: ResponseMeta? = null
    ) : NetworkResult<T>()

    data class Error(
        val error: ApiError
    ) : NetworkResult<Nothing>()

    data object Loading : NetworkResult<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val isLoading: Boolean
        get() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    inline fun <R> map(transform: (T) -> R): NetworkResult<R> = when (this) {
        is Success -> Success(transform(data), message, meta)
        is Error -> this
        is Loading -> this
    }

    inline fun onSuccess(action: (T) -> Unit): NetworkResult<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (ApiError) -> Unit): NetworkResult<T> {
        if (this is Error) action(error)
        return this
    }
}
