package az.less.mobile.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import az.less.mobile.data.datasource.FavoritesDataSource
import az.less.mobile.data.paging.FavoritesPagingSource
import az.less.mobile.domain.repository.FavoritesRepository
import az.less.mobile.presentation.client.main.favorites.models.FavoriteMerchant
import kotlinx.coroutines.flow.Flow

class FavoritesRepositoryImpl(
    private val favoritesDataSource: FavoritesDataSource
) : FavoritesRepository {

    override fun getFavorites(
        latitude: Double?,
        longitude: Double?
    ): Flow<PagingData<FavoriteMerchant>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            FavoritesPagingSource(
                dataSource = favoritesDataSource,
                latitude = latitude,
                longitude = longitude
            )
        }
    ).flow
}
