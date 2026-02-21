package az.less.mobile.domain.repository

import androidx.paging.PagingData
import az.less.mobile.presentation.client.main.favorites.models.FavoriteMerchant
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavorites(
        latitude: Double?,
        longitude: Double?
    ): Flow<PagingData<FavoriteMerchant>>
}
