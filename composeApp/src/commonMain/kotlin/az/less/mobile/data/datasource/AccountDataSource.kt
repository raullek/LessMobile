package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.account.UpdateUserData
import az.less.mobile.data.remote.model.account.UpdateUserRequest
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.setBody

class AccountDataSource(
    private val httpClient: HttpClient
) {
    suspend fun updateUser(
        userId: String,
        request: UpdateUserRequest
    ): NetworkResult<UpdateUserData> {
        return safeApiCall {
            httpClient.patch("v1/users/$userId") {
                setBody(request)
            }
        }
    }

    suspend fun deleteProfile(): NetworkResult<Unit> {
        return safeApiCall {
            httpClient.delete("v1/users/profile")
        }
    }
}
