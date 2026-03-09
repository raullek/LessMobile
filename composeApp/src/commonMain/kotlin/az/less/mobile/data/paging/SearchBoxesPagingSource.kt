package az.less.mobile.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import az.less.mobile.data.datasource.ExploreDataSource
import az.less.mobile.domain.model.FilterBox
import az.less.mobile.domain.model.toDomain
import az.less.mobile.network.NetworkResult

class SearchBoxesPagingSource(
    private val dataSource: ExploreDataSource,
    private val filtersJson: String,
    private val latitude: Double?,
    private val longitude: Double?
) : PagingSource<Int, FilterBox>() {

    override fun getRefreshKey(state: PagingState<Int, FilterBox>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilterBox> {
        val page = params.key ?: 1

        return when (val result = dataSource.searchBoxesByFilters(
            filters = filtersJson,
            page = page,
            limit = params.loadSize,
            latitude = latitude,
            longitude = longitude,
            returnVenueForBox = true
        )) {
            is NetworkResult.Success -> {
                val boxes = result.data.boxes.map { it.toDomain() }
                val total = result.data.pagination?.total ?: result.data.total

                LoadResult.Page(
                    data = boxes,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (boxes.size < params.loadSize || (page * params.loadSize) >= total) null else page + 1
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
