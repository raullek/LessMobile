package az.less.mobile.presentation.client.main.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.FavoritesRepository
import az.less.mobile.domain.repository.MerchantRepository
import az.less.mobile.utils.formatOneDecimal
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.withTimeoutOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

private const val MAX_REVIEWS_LIMIT = 50
private const val MAX_OFFERS_LIMIT = 50

class MerchantProfileViewModel(
    private val merchantRepository: MerchantRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel(), ContainerHost<MerchantProfileState, MerchantProfileSideEffect> {

    override val container: Container<MerchantProfileState, MerchantProfileSideEffect> =
        viewModelScope.container(MerchantProfileState())

    private val geolocator: Geolocator = Geolocator.mobile()
    private var isInitialized = false

    fun initialize(merchantId: String) {
        if (isInitialized) return
        isInitialized = true
        loadMerchantProfile(merchantId)
    }

    fun onIntent(intent: MerchantProfileIntent) {
        when (intent) {
            is MerchantProfileIntent.OnBackClicked -> handleBackClicked()
            is MerchantProfileIntent.OnFavoriteClicked -> handleFavoriteClicked()
            is MerchantProfileIntent.OnTabSelected -> handleTabSelected(intent.tab)
            is MerchantProfileIntent.OnDirectionsClicked -> handleDirectionsClicked()
            is MerchantProfileIntent.OnPhoneClicked -> handlePhoneClicked()
            is MerchantProfileIntent.OnViewLocationClicked -> handleViewLocationClicked()
            is MerchantProfileIntent.OnMapBackClicked -> handleMapBackClicked()
            is MerchantProfileIntent.OnOfferClicked -> handleOfferClicked(intent.offerId)
        }
    }

    private fun loadMerchantProfile(merchantId: String) = intent {
        reduce { state.copy(isLoading = true, merchantId = merchantId) }

        val location = getUserLocation()
        val userLat = location?.first
        val userLng = location?.second

        merchantRepository.getMerchantProfile(
            merchantId = merchantId,
            includeReviews = true,
            reviewsLimit = MAX_REVIEWS_LIMIT,
            includeOffers = true,
            offersLimit = MAX_OFFERS_LIMIT,
            latitude = userLat,
            longitude = userLng
        )
            .onSuccess { profile ->
                reduce {
                    state.copy(
                        isLoading = false,
                        merchantId = profile.id,
                        merchantName = profile.name,
                        merchantDescription = profile.businessDescription,
                        merchantLogoUrl = profile.businessLogo,
                        heroImageUrl = profile.coverImage,
                        rating = profile.rating,
                        ratingCount = profile.ratingCount,
                        ratingDistribution = profile.ratingDistribution,
                        distance = profile.distanceKm?.let { formatDistance(it) } ?: "",
                        phoneNumber = profile.phone,
                        address = profile.businessAddress,
                        latitude = profile.latitude,
                        longitude = profile.longitude,
                        offers = profile.offers,
                        reviews = profile.reviews,
                        reviewsTotal = profile.reviewsTotal,
                        reviewsHasMore = profile.reviewsHasMore,
                        isFavorite = profile.isFavorite,
                        favoriteId = profile.favoriteId
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(MerchantProfileSideEffect.ShowError(error.message))
            }
    }

    private suspend fun getUserLocation(): Pair<Double, Double>? {
        return withTimeoutOrNull(3000L) {
            if (geolocator.isAvailable()) {
                geolocator.current().getOrNull()?.let {
                    Pair(it.coordinates.latitude, it.coordinates.longitude)
                }
            } else null
        }
    }

    private fun formatDistance(km: Double): String {
        return if (km < 1.0) {
            "${(km * 1000).toInt()} m"
        } else {
            "${km.formatOneDecimal()} km"
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(MerchantProfileSideEffect.NavigateBack)
    }

    private fun handleFavoriteClicked() = intent {
        val wasFavorite = state.isFavorite
        val oldFavoriteId = state.favoriteId

        // Optimistic UI toggle
        reduce { state.copy(isFavorite = !wasFavorite) }

        if (!wasFavorite) {
            // Adding to favorites
            favoritesRepository.addFavorite(state.merchantId)
                .onSuccess { response ->
                    reduce { state.copy(favoriteId = response.id) }
                }
                .onError { error ->
                    reduce { state.copy(isFavorite = false, favoriteId = oldFavoriteId) }
                    postSideEffect(MerchantProfileSideEffect.ShowError(error.message))
                }
        } else {
            // Removing from favorites. If the profile response didn't include a
            // favoriteId, fall back to GET /v1/favorites/check?venueId=… which
            // returns the id we need for DELETE.
            val idToDelete = oldFavoriteId
                ?: favoritesRepository.checkFavorite(state.merchantId).getOrNull()?.favoriteId

            if (idToDelete == null) {
                reduce { state.copy(isFavorite = true, favoriteId = oldFavoriteId) }
                postSideEffect(MerchantProfileSideEffect.ShowError("Could not resolve favorite id"))
                return@intent
            }

            favoritesRepository.removeFavorite(idToDelete)
                .onSuccess {
                    reduce { state.copy(favoriteId = null) }
                }
                .onError { error ->
                    reduce { state.copy(isFavorite = true, favoriteId = oldFavoriteId) }
                    postSideEffect(MerchantProfileSideEffect.ShowError(error.message))
                }
        }
    }

    private fun handleTabSelected(tab: MerchantProfileTab) = intent {
        reduce { state.copy(selectedTab = tab) }
    }

    private fun handleDirectionsClicked() = intent {
        postSideEffect(MerchantProfileSideEffect.OpenDirections(state.address))
    }

    private fun handlePhoneClicked() = intent {
        postSideEffect(MerchantProfileSideEffect.CallPhone(state.phoneNumber))
    }

    private fun handleViewLocationClicked() = intent {
        reduce { state.copy(isMapVisible = true) }
    }

    private fun handleMapBackClicked() = intent {
        reduce { state.copy(isMapVisible = false) }
    }

    private fun handleOfferClicked(offerId: String) = intent {
        postSideEffect(MerchantProfileSideEffect.NavigateToReserve(offerId))
    }
}
