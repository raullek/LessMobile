package az.less.mobile.domain.repository

import androidx.paging.PagingData
import az.less.mobile.data.remote.model.SearchFilterDto
import az.less.mobile.domain.model.FilterBox
import az.less.mobile.domain.model.SearchBoxesResult
import az.less.mobile.domain.model.SearchVenue
import az.less.mobile.domain.model.SearchVenuesResult
import az.less.mobile.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {

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
    ): NetworkResult<SearchVenuesResult>

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
    ): NetworkResult<SearchBoxesResult>

    suspend fun getBoxesForVenue(venueId: String): NetworkResult<SearchBoxesResult>

    suspend fun getSearchFilters(): NetworkResult<List<SearchFilterDto>>

    fun searchVenues(
        query: String,
        latitude: Double?,
        longitude: Double?
    ): Flow<PagingData<SearchVenue>>

    fun searchBoxesByFiltersPaged(
        filtersJson: String,
        latitude: Double?,
        longitude: Double?
    ): Flow<PagingData<FilterBox>>
}
