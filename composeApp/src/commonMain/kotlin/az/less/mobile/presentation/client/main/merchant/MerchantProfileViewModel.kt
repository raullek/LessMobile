package az.less.mobile.presentation.client.main.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.MerchantRepository
import az.less.mobile.utils.formatOneDecimal
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.withTimeoutOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class MerchantProfileViewModel(
    private val merchantRepository: MerchantRepository
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
                        reviews = profile.reviews,
                        reviewsTotal = profile.reviewsTotal,
                        reviewsHasMore = profile.reviewsHasMore
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
        reduce { state.copy(isFavorite = !state.isFavorite) }
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
}
