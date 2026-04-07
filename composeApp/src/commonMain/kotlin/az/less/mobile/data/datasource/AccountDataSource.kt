package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.UserProfileData
import az.less.mobile.data.remote.model.UserProfileUser
import az.less.mobile.data.remote.model.account.UpdateUserRequest
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody

class AccountDataSource(
    private val httpClient: HttpClient
) {
    suspend fun updateUser(
        request: UpdateUserRequest
    ): NetworkResult<UserProfileUser> {
        return safeApiCall {
            httpClient.patch("v1/users/profile") {
                setBody(request)
            }
        }
    }

    suspend fun getProfile(): NetworkResult<UserProfileData> {
        return safeApiCall {
            httpClient.get("v1/users/profile")
        }
    }

    suspend fun deleteProfile(): NetworkResult<Unit> {
        return safeApiCall {
            httpClient.delete("v1/users/profile")
        }
    }
}
