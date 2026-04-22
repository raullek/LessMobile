package az.less.mobile.domain.repository

import androidx.paging.PagingData
import az.less.mobile.data.remote.model.AddFavoriteResponse
import az.less.mobile.data.remote.model.CheckFavoriteResponse
import az.less.mobile.data.remote.model.RemoveFavoriteResponse
import az.less.mobile.network.NetworkResult
import az.less.mobile.presentation.client.main.favorites.models.FavoriteMerchant
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavorites(
        latitude: Double?,
        longitude: Double?
    ): Flow<PagingData<FavoriteMerchant>>

    suspend fun addFavorite(venueId: String): NetworkResult<AddFavoriteResponse>

    suspend fun removeFavorite(favoriteId: String): NetworkResult<RemoveFavoriteResponse>

    suspend fun checkFavorite(venueId: String): NetworkResult<CheckFavoriteResponse>
}
