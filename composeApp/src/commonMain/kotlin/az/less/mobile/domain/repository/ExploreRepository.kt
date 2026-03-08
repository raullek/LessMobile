package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.SearchFilterDto
import az.less.mobile.domain.model.UnifiedSearchData
import az.less.mobile.network.NetworkResult

interface ExploreRepository {
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
    ): NetworkResult<UnifiedSearchData>

    suspend fun getSearchFilters(): NetworkResult<List<SearchFilterDto>>
}
