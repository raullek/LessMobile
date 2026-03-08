package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.ContentDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ContentDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getTerms(): NetworkResult<ContentDto> {
        return safeApiCall {
            httpClient.get("v1/content/terms")
        }
    }
}
