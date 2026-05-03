package az.less.mobile.presentation.partner.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.MerchantRepository
import az.less.mobile.presentation.client.main.merchant.MerchantProfileTab
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

private const val MAX_REVIEWS_LIMIT = 50
private const val MAX_OFFERS_LIMIT = 50

class VenuePreviewViewModel(
    private val merchantRepository: MerchantRepository
) : ViewModel(), ContainerHost<VenuePreviewState, VenuePreviewSideEffect> {

    override val container: Container<VenuePreviewState, VenuePreviewSideEffect> =
        viewModelScope.container(VenuePreviewState())

    private var isInitialized = false

    fun initialize(venueId: String) {
        if (isInitialized) return
        isInitialized = true
        loadProfile(venueId)
    }

    fun onIntent(intent: VenuePreviewIntent) {
        when (intent) {
            is VenuePreviewIntent.OnBackClicked -> handleBack()
            is VenuePreviewIntent.OnTabSelected -> handleTabSelected(intent.tab)
            is VenuePreviewIntent.OnViewLocationClicked -> handleViewLocation()
            is VenuePreviewIntent.OnMapBackClicked -> handleMapBack()
        }
    }

    private fun loadProfile(venueId: String) = intent {
        reduce { state.copy(isLoading = true, venueId = venueId) }

        merchantRepository.getMerchantProfile(
            merchantId = venueId,
            includeReviews = true,
            reviewsLimit = MAX_REVIEWS_LIMIT,
            includeOffers = true,
            offersLimit = MAX_OFFERS_LIMIT
        )
            .onSuccess { profile ->
                reduce {
                    state.copy(
                        isLoading = false,
                        venueId = profile.id,
                        merchantName = profile.name,
                        merchantDescription = profile.businessDescription,
                        merchantLogoUrl = profile.businessLogo,
                        heroImageUrl = profile.coverImage,
                        rating = profile.rating,
                        ratingCount = profile.ratingCount,
                        ratingDistribution = profile.ratingDistribution,
                        phoneNumber = profile.phone,
                        address = profile.businessAddress,
                        latitude = profile.latitude,
                        longitude = profile.longitude,
                        offers = profile.offers,
                        reviews = profile.reviews,
                        reviewsTotal = profile.reviewsTotal
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(VenuePreviewSideEffect.ShowError(error.message))
            }
    }

    private fun handleBack() = intent {
        if (state.isMapVisible) {
            reduce { state.copy(isMapVisible = false) }
        } else {
            postSideEffect(VenuePreviewSideEffect.NavigateBack)
        }
    }

    private fun handleTabSelected(tab: MerchantProfileTab) = intent {
        reduce { state.copy(selectedTab = tab) }
    }

    private fun handleViewLocation() = intent {
        reduce { state.copy(isMapVisible = true) }
    }

    private fun handleMapBack() = intent {
        reduce { state.copy(isMapVisible = false) }
    }
}
