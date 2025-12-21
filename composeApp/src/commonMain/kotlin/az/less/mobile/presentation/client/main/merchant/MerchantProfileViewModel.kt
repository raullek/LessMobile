package az.less.mobile.presentation.client.main.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.client.main.merchant.models.MerchantOfferItem
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Merchant Profile Screen using Orbit MVI
 */
class MerchantProfileViewModel : ViewModel(), ContainerHost<MerchantProfileState, MerchantProfileSideEffect> {

    override val container: Container<MerchantProfileState, MerchantProfileSideEffect> =
        viewModelScope.container(MerchantProfileState())

    init {
        loadInitialData()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: MerchantProfileIntent) {
        when (intent) {
            is MerchantProfileIntent.OnBackClicked -> handleBackClicked()
            is MerchantProfileIntent.OnFavoriteClicked -> handleFavoriteClicked()
            is MerchantProfileIntent.OnTabSelected -> handleTabSelected(intent.tab)
            is MerchantProfileIntent.OnOfferClicked -> handleOfferClicked(intent.offerId)
            is MerchantProfileIntent.OnDirectionsClicked -> handleDirectionsClicked()
            is MerchantProfileIntent.OnPhoneClicked -> handlePhoneClicked()
            is MerchantProfileIntent.OnViewLocationClicked -> handleViewLocationClicked()
            is MerchantProfileIntent.OnMapBackClicked -> handleMapBackClicked()
        }
    }

    private fun loadInitialData() = intent {
        reduce {
            state.copy(
                merchantId = "merchant_1",
                merchantName = "Belgian Chocolate & Coffee",
                merchantDescription = "Indulge in rich Belgian flavors and smooth specialty coffee crafted with care.",
                rating = 4.9f,
                distance = "1.2 km",
                phoneNumber = "+994 55 555 65 78",
                address = "1 Neftçilər Prospekti, Bakı 1095",
                latitude = 40.3725,
                longitude = 49.8533,
                isFavorite = false,
                offers = getMockOffers()
            )
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(MerchantProfileSideEffect.NavigateBack)
    }

    private fun handleFavoriteClicked() = intent {
        reduce {
            state.copy(isFavorite = !state.isFavorite)
        }
    }

    private fun handleTabSelected(tab: MerchantProfileTab) = intent {
        reduce {
            state.copy(selectedTab = tab)
        }
    }

    private fun handleOfferClicked(offerId: String) = intent {
        postSideEffect(MerchantProfileSideEffect.NavigateToReserve(offerId))
    }

    private fun handleDirectionsClicked() = intent {
        postSideEffect(MerchantProfileSideEffect.OpenDirections(state.address))
    }

    private fun handlePhoneClicked() = intent {
        postSideEffect(MerchantProfileSideEffect.CallPhone(state.phoneNumber))
    }

    private fun handleViewLocationClicked() = intent {
        reduce {
            state.copy(isMapVisible = true)
        }
    }

    private fun handleMapBackClicked() = intent {
        reduce {
            state.copy(isMapVisible = false)
        }
    }

    // Mock data - replace with repository calls in real app
    private fun getMockOffers(): List<MerchantOfferItem> {
        return listOf(
            MerchantOfferItem(
                id = "1",
                merchantName = "Belgian Chocolate & Coffee",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                distance = "1.2 km",
                itemsOnSale = 12,
                hasActiveDiscount = true
            ),
            MerchantOfferItem(
                id = "2",
                merchantName = "Belgian Chocolate & Coffee",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                distance = "1.2 km",
                itemsOnSale = 0,
                hasActiveDiscount = false
            ),
            MerchantOfferItem(
                id = "3",
                merchantName = "Belgian Chocolate & Coffee",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                distance = "1.2 km",
                itemsOnSale = 12,
                hasActiveDiscount = true
            )
        )
    }
}
