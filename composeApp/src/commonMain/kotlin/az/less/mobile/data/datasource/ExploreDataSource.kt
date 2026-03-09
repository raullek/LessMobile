package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.SearchByFilterBoxesDto
import az.less.mobile.data.remote.model.SearchByFilterVenuesDto
import az.less.mobile.data.remote.model.SearchFilterDto
import az.less.mobile.data.remote.model.SearchVenuesResponseDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ExploreDataSource(
    private val httpClient: HttpClient
) {
    suspend fun searchVenuesByFilters(
        filters: String = "[]",
        page: Int? = null,
        limit: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        isFavorite: Boolean? = null,
        nearest: Boolean? = null,
        hotDeals: Boolean? = null,
        openNow: Boolean? = null,
        returnBoxesForVenue: Boolean? = null
    ): NetworkResult<SearchByFilterVenuesDto> {
        return safeApiCall {
            httpClient.get("v1/search/by-filters") {
                parameter("type", "venues")
                parameter("filters", filters)
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
                latitude?.let { parameter("latitude", it) }
                longitude?.let { parameter("longitude", it) }
                isFavorite?.let { parameter("isFavorite", it) }
                nearest?.let { parameter("nearest", it) }
                hotDeals?.let { parameter("hotDeals", it) }
                openNow?.let { parameter("openNow", it) }
                returnBoxesForVenue?.let { parameter("returnBoxesForVenue", it) }
            }
        }
    }

    suspend fun searchBoxesByFilters(
        filters: String = "[]",
        page: Int? = null,
        limit: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        isFavorite: Boolean? = null,
        nearest: Boolean? = null,
        hotDeals: Boolean? = null,
        openNow: Boolean? = null,
        venueId: String? = null,
        returnVenueForBox: Boolean? = null
    ): NetworkResult<SearchByFilterBoxesDto> {
        return safeApiCall {
            httpClient.get("v1/search/by-filters") {
                parameter("type", "boxes")
                parameter("filters", filters)
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
                latitude?.let { parameter("latitude", it) }
                longitude?.let { parameter("longitude", it) }
                isFavorite?.let { parameter("isFavorite", it) }
                nearest?.let { parameter("nearest", it) }
                hotDeals?.let { parameter("hotDeals", it) }
                openNow?.let { parameter("openNow", it) }
                venueId?.let { parameter("venueId", it) }
                returnVenueForBox?.let { parameter("returnVenueForBox", it) }
            }
        }
    }

    suspend fun getBoxesForVenue(
        venueId: String
    ): NetworkResult<SearchByFilterBoxesDto> {
        return safeApiCall {
            httpClient.get("v1/search/by-filters") {
                parameter("type", "boxes")
                parameter("filters", "[]")
                parameter("venueId", venueId)
            }
        }
    }

    suspend fun getSearchFilters(): NetworkResult<List<SearchFilterDto>> {
        return safeApiCall {
            httpClient.get("v1/search/filters")
        }
    }

    suspend fun searchVenues(
        q: String,
        page: Int? = null,
        limit: Int? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        returnBoxesForVenue: Boolean? = null
    ): NetworkResult<SearchVenuesResponseDto> {
        return safeApiCall {
            httpClient.get("v1/search/venues") {
                parameter("q", q)
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
                latitude?.let { parameter("latitude", it) }
                longitude?.let { parameter("longitude", it) }
                returnBoxesForVenue?.let { parameter("returnBoxesForVenue", it) }
            }
        }
    }
}
