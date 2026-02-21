package az.less.mobile.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import az.less.mobile.data.datasource.FavoritesDataSource
import az.less.mobile.data.remote.model.FavoriteItemDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.presentation.client.main.favorites.models.FavoriteMerchant

class FavoritesPagingSource(
    private val dataSource: FavoritesDataSource,
    private val latitude: Double?,
    private val longitude: Double?
) : PagingSource<Int, FavoriteMerchant>() {

    override fun getRefreshKey(state: PagingState<Int, FavoriteMerchant>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavoriteMerchant> {
        val page = params.key ?: 1

        return when (val result = dataSource.getFavorites(latitude, longitude, page, params.loadSize)) {
            is NetworkResult.Success -> {
                val data = result.data
                val merchants = data.data.map { it.toDomain() }
                val pagination = data.pagination

                LoadResult.Page(
                    data = merchants,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (pagination?.hasNext == true) page + 1 else null
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

private fun FavoriteItemDto.toDomain() = FavoriteMerchant(
    id = venue?.id ?: id,
    merchantName = venue?.name ?: itemSnapshot?.name ?: "",
    address = venue?.address ?: itemSnapshot?.address ?: "",
    imageUrl = null,
    merchantLogoUrl = venue?.logo ?: itemSnapshot?.logo,
    rating = venue?.rating?.toFloat() ?: 0f,
    distance = venue?.distanceKm?.toString().orEmpty(),
    itemsOnSale = venue?.itemsOnSale ?: 0,
    badgeText = venue?.badge?.text
)
