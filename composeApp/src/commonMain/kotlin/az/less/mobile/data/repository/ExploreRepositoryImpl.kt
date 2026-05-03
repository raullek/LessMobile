package az.less.mobile.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import az.less.mobile.data.datasource.ExploreDataSource
import az.less.mobile.data.paging.SearchBoxesPagingSource
import az.less.mobile.data.paging.SearchVenuesPagingSource
import az.less.mobile.data.remote.model.CategoryItemDto
import az.less.mobile.data.remote.model.SearchFilterDto
import az.less.mobile.domain.model.FilterBox
import az.less.mobile.domain.model.SearchBoxesResult
import az.less.mobile.domain.model.SearchVenue
import az.less.mobile.domain.model.SearchVenuesResult
import az.less.mobile.domain.model.toDomain
import az.less.mobile.domain.repository.ExploreRepository
import az.less.mobile.network.NetworkResult
import kotlinx.coroutines.flow.Flow

class ExploreRepositoryImpl(
    private val exploreDataSource: ExploreDataSource
) : ExploreRepository {

    override suspend fun searchVenuesByFilters(
        filters: String,
        page: Int?,
        limit: Int?,
        latitude: Double?,
        longitude: Double?,
        isFavorite: Boolean?,
        nearest: Boolean?,
        hotDeals: Boolean?,
        openNow: Boolean?,
        returnBoxesForVenue: Boolean?
    ): NetworkResult<SearchVenuesResult> {
        return exploreDataSource.searchVenuesByFilters(
            filters, page, limit, latitude, longitude,
            isFavorite, nearest, hotDeals, openNow, returnBoxesForVenue
        ).map { it.toDomain() }
    }

    override suspend fun searchBoxesByFilters(
        filters: String,
        page: Int?,
        limit: Int?,
        latitude: Double?,
        longitude: Double?,
        isFavorite: Boolean?,
        nearest: Boolean?,
        hotDeals: Boolean?,
        openNow: Boolean?,
        venueId: String?,
        returnVenueForBox: Boolean?
    ): NetworkResult<SearchBoxesResult> {
        return exploreDataSource.searchBoxesByFilters(
            filters, page, limit, latitude, longitude,
            isFavorite, nearest, hotDeals, openNow, venueId, returnVenueForBox
        ).map { it.toDomain() }
    }

    override suspend fun getBoxesForVenue(venueId: String): NetworkResult<SearchBoxesResult> {
        return exploreDataSource.getBoxesForVenue(venueId).map { it.toDomain() }
    }

    override suspend fun getCategories(
        page: Int?,
        limit: Int?
    ): NetworkResult<List<CategoryItemDto>> {
        return exploreDataSource.getCategories(page, limit)
    }

    override suspend fun getSearchFilters(): NetworkResult<List<SearchFilterDto>> {
        return exploreDataSource.getSearchFilters()
    }

    override fun searchVenues(
        query: String,
        latitude: Double?,
        longitude: Double?
    ): Flow<PagingData<SearchVenue>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            SearchVenuesPagingSource(
                dataSource = exploreDataSource,
                query = query,
                latitude = latitude,
                longitude = longitude
            )
        }
    ).flow

    override fun searchBoxesByFiltersPaged(
        filtersJson: String,
        latitude: Double?,
        longitude: Double?
    ): Flow<PagingData<FilterBox>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            SearchBoxesPagingSource(
                dataSource = exploreDataSource,
                filtersJson = filtersJson,
                latitude = latitude,
                longitude = longitude
            )
        }
    ).flow
}
