package az.less.mobile.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import az.less.mobile.data.datasource.ExploreDataSource
import az.less.mobile.domain.model.SearchVenue
import az.less.mobile.domain.model.toDomain
import az.less.mobile.network.NetworkResult

class SearchVenuesPagingSource(
    private val dataSource: ExploreDataSource,
    private val query: String,
    private val latitude: Double?,
    private val longitude: Double?
) : PagingSource<Int, SearchVenue>() {

    override fun getRefreshKey(state: PagingState<Int, SearchVenue>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchVenue> {
        val page = params.key ?: 1

        return when (val result = dataSource.searchVenues(
            q = query,
            page = page,
            limit = params.loadSize,
            latitude = latitude,
            longitude = longitude
        )) {
            is NetworkResult.Success -> {
                val venues = result.data.data.map { it.toDomain() }

                LoadResult.Page(
                    data = venues,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (venues.size < params.loadSize) null else page + 1
                )
            }

            is NetworkResult.Error -> {
                LoadResult.Error(Exception(result.error.message))
            }

            is NetworkResult.Loading -> {
                LoadResult.Error(Exception("Unexpected loading state"))
            }
        }
    }
}
