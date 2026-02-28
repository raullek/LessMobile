package az.less.mobile.data.repository

import az.less.mobile.data.datasource.ExploreDataSource
import az.less.mobile.data.remote.model.SearchFilterDto
import az.less.mobile.domain.model.UnifiedSearchData
import az.less.mobile.domain.model.toDomain
import az.less.mobile.domain.repository.ExploreRepository
import az.less.mobile.network.NetworkResult

class ExploreRepositoryImpl(
    private val exploreDataSource: ExploreDataSource
) : ExploreRepository {

    override suspend fun unifiedSearch(
        query: String?,
        tags: String?,
        category: String?,
        timeRange: String?,
        pickupTime: String?,
        pickupDate: String?,
        pickupDay: String?,
        openNow: Boolean?,
        sortBy: String?,
        limit: Int?,
        page: Int?,
        longitude: Double?,
        latitude: Double?
    ): NetworkResult<UnifiedSearchData> {
        return exploreDataSource.unifiedSearch(
            query, tags, category, timeRange, pickupTime,
            pickupDate, pickupDay, openNow, sortBy, limit, page,
            longitude, latitude
        ).map { it.toDomain() }
    }

    override suspend fun getSearchFilters(): NetworkResult<List<SearchFilterDto>> {
        return exploreDataSource.getSearchFilters()
    }
}
