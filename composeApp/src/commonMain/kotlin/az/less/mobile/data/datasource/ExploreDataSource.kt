package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.SearchFilterDto
import az.less.mobile.data.remote.model.UnifiedSearchDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ExploreDataSource(
    private val httpClient: HttpClient
) {
    suspend fun unifiedSearch(
        query: String? = null,
        tags: String? = null,
        category: String? = null,
        timeRange: String? = null,
        pickupTime: String? = null,
        pickupDate: String? = null,
        pickupDay: String? = null,
        openNow: Boolean? = null,
        sortBy: String? = null,
        limit: Int? = null,
        page: Int? = null,
        longitude: Double? = null,
        latitude: Double? = null
    ): NetworkResult<UnifiedSearchDto> {
        return safeApiCall {
            httpClient.get("v1/search/unified") {
                query?.let { parameter("query", it) }
                tags?.let { parameter("tags", it) }
                category?.let { parameter("category", it) }
                timeRange?.let { parameter("timeRange", it) }
                pickupTime?.let { parameter("pickupTime", it) }
                pickupDate?.let { parameter("pickupDate", it) }
                pickupDay?.let { parameter("pickupDay", it) }
                openNow?.let { parameter("openNow", it) }
                sortBy?.let { parameter("sortBy", it) }
                limit?.let { parameter("limit", it) }
                page?.let { parameter("page", it) }
                longitude?.let { parameter("longitude", it) }
                latitude?.let { parameter("latitude", it) }
            }
        }
    }

    suspend fun getSearchFilters(): NetworkResult<List<SearchFilterDto>> {
        return safeApiCall {
            httpClient.get("v1/search/filters")
        }
    }
}
