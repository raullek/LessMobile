package az.less.mobile.presentation.client.main.categoryoffers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.client.main.offers.models.OfferItem
import az.less.mobile.presentation.client.main.offers.models.OfferMerchant
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategory
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_mark_16dp
import lessmobile.composeapp.generated.resources.ic_star_16dp
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class CategoryOffersViewModel : ViewModel(), ContainerHost<CategoryOffersState, CategoryOffersSideEffect> {

    override val container: Container<CategoryOffersState, CategoryOffersSideEffect> =
        viewModelScope.container(CategoryOffersState())

    private var isInitialized = false

    fun initialize(categoryId: String, categoryType: String, categoryTitle: String) {
        if (isInitialized) return
        isInitialized = true

        intent {
            reduce {
                state.copy(
                    categoryId = categoryId,
                    categoryType = categoryType,
                    categoryTitle = categoryTitle
                )
            }
            loadOffers()
        }
    }

    private suspend fun loadOffers() {
        intent {
            reduce {
                state.copy(
                    segmentedCategories = getMockSegmentedCategories(),
                    offers = getMockOffers(state.categoryId, state.selectedSegmentId)
                )
            }
        }
    }

    fun onIntent(intent: CategoryOffersIntent) {
        when (intent) {
            is CategoryOffersIntent.OnBackClicked -> handleBackClicked()
            is CategoryOffersIntent.OnSegmentSelected -> handleSegmentSelected(intent.segmentId)
            is CategoryOffersIntent.OnOfferItemClicked -> handleOfferItemClicked(intent.offerId)
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(CategoryOffersSideEffect.NavigateBack)
    }

    private fun handleSegmentSelected(segmentId: String) = intent {
        val newSelectedId = if (state.selectedSegmentId == segmentId) null else segmentId
        reduce {
            state.copy(
                selectedSegmentId = newSelectedId,
                offers = getMockOffers(state.categoryId, newSelectedId)
            )
        }
    }

    private fun handleOfferItemClicked(offerId: String) = intent {
        val offerItem = state.offers.find { it.id == offerId }
        if (offerItem != null) {
            postSideEffect(CategoryOffersSideEffect.NavigateToReserve(offerItem))
        }
    }

    // ==================== Mock Data (TODO: replace with API) ====================

    private fun getMockSegmentedCategories(): List<SegmentedCategory> {
        return listOf(
            SegmentedCategory(
                id = "nearest", type = "NEAREST", title = "Nearest",
                icon = Res.drawable.ic_explore_24dp, iconTint = 0xFFFF8B38L
            ),
            SegmentedCategory(
                id = "top_rated", type = "TOP_RATED", title = "Top rated",
                icon = Res.drawable.ic_star_16dp, iconTint = 0xFF5AA9E7L
            ),
            SegmentedCategory(
                id = "hot_deals", type = "HOT_DEALS", title = "Hot deals",
                icon = Res.drawable.ic_mark_16dp, iconTint = 0xFFAD3CDAL
            )
        )
    }

    private fun getMockOffers(categoryId: String, segmentId: String? = null): List<OfferItem> {
        val baseOffers = when (segmentId) {
            "nearest" -> getNearestOffers()
            "top_rated" -> getTopRatedOffers()
            "hot_deals" -> getHotDealsOffers()
            else -> getAllOffers()
        }
        return baseOffers
    }

    private fun getAllOffers(): List<OfferItem> {
        return listOf(
            OfferItem(
                id = "cat_offer_1",
                title = "Classic Burger",
                description = "Burgers",
                imageBgColor = "#fff2eb",
                quantity = 8,
                originalPrice = "15.99",
                currentPrice = "12.99",
                bagType = "Small Bag",
                category = "Burgers",
                pickupTime = "17:00 - 23:00",
                merchant = OfferMerchant(
                    id = "merchant_1",
                    name = "Burger House",
                    latitude = 40.4093,
                    longitude = 49.8671,
                    rating = 4.8f
                )
            ),
            OfferItem(
                id = "cat_offer_2",
                title = "Cheese Burger",
                description = "Burgers",
                imageBgColor = "#fff2eb",
                quantity = 5,
                originalPrice = "18.99",
                currentPrice = "14.99",
                bagType = "Medium Bag",
                category = "Burgers",
                pickupTime = "18:00 - 22:00",
                merchant = OfferMerchant(
                    id = "merchant_2",
                    name = "Burger King",
                    latitude = 40.4047,
                    longitude = 49.8687,
                    rating = 4.6f
                )
            ),
            OfferItem(
                id = "cat_offer_3",
                title = "Veggie Burger",
                description = "Vegetarian",
                imageBgColor = "#fff2eb",
                quantity = 12,
                originalPrice = "13.99",
                currentPrice = "10.99",
                bagType = "Small Bag",
                category = "Vegetarian",
                pickupTime = "16:00 - 21:00",
                merchant = OfferMerchant(
                    id = "merchant_3",
                    name = "Green Burger",
                    latitude = 40.4295,
                    longitude = 49.8535,
                    rating = 4.9f
                )
            ),
            OfferItem(
                id = "cat_offer_4",
                title = "BBQ Burger",
                description = "Burgers",
                imageBgColor = "#fff2eb",
                quantity = 3,
                originalPrice = "19.99",
                currentPrice = "16.99",
                bagType = "Large Bag",
                category = "Burgers",
                pickupTime = "17:30 - 23:30",
                merchant = OfferMerchant(
                    id = "merchant_4",
                    name = "BBQ Place",
                    latitude = 40.4436,
                    longitude = 49.8671,
                    rating = 4.7f
                )
            ),
            OfferItem(
                id = "cat_offer_5",
                title = "Double Burger",
                description = "Burgers",
                imageBgColor = "#fff2eb",
                quantity = 6,
                originalPrice = "22.99",
                currentPrice = "18.99",
                bagType = "Large Bag",
                category = "Burgers",
                pickupTime = "19:00 - 23:00",
                merchant = OfferMerchant(
                    id = "merchant_5",
                    name = "Mega Burger",
                    latitude = 40.3910,
                    longitude = 49.8753,
                    rating = 4.5f
                )
            )
        )
    }

    private fun getNearestOffers(): List<OfferItem> {
        // TODO: sort by actual distance from user location
        return getAllOffers()
    }

    private fun getTopRatedOffers(): List<OfferItem> {
        return getAllOffers().sortedByDescending { it.merchant.rating }
    }

    private fun getHotDealsOffers(): List<OfferItem> {
        return getAllOffers().sortedByDescending {
            val original = it.originalPrice.toFloatOrNull() ?: 0f
            val current = it.currentPrice.toFloatOrNull() ?: 0f
            original - current
        }
    }
}
