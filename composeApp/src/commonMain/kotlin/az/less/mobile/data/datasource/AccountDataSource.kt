package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.UserProfileData
import az.less.mobile.data.remote.model.account.UpdateUserData
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

class AccountDataSource(
    private val httpClient: HttpClient
) {
    suspend fun updateUser(
        userId: String,
        name: String?,
        phone: String?,
        gender: String?,
        birthDay: String?,
        avatar: ByteArray?
    ): NetworkResult<UpdateUserData> {
        return safeApiCall {
            httpClient.submitFormWithBinaryData(
                url = "v1/users/$userId",
                formData = formData {
                    name?.let { append("name", it) }
                    phone?.let { append("phone", it) }
                    gender?.let { append("gender", it) }
                    birthDay?.let { append("birthDay", it) }
                    avatar?.let {
                        append("avatar", it, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"avatar.jpg\"")
                        })
                    }
                }
            ) {
                method = HttpMethod.Patch
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
