package az.less.mobile.presentation.main.categoryoffers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.offers.models.FilterSegment
import az.less.mobile.presentation.main.offers.models.FilterSegmentType
import az.less.mobile.presentation.main.offers.models.OfferItem
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_mark_16dp
import lessmobile.composeapp.generated.resources.ic_star_16dp
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Category Offers Screen using Orbit MVI
 */
class CategoryOffersViewModel : ViewModel(), ContainerHost<CategoryOffersState, CategoryOffersSideEffect> {
    
    override val container: Container<CategoryOffersState, CategoryOffersSideEffect> = 
        viewModelScope.container(
            CategoryOffersState(
                categoryType = "all",
                categoryTitle = "All Offers"
            )
        )
    
    init {
        loadInitialData()
    }
    
    /**
     * Handle user intents
     */
    fun onIntent(intent: CategoryOffersIntent) {
        when (intent) {
            is CategoryOffersIntent.OnBackClicked -> handleBackClicked()
            is CategoryOffersIntent.OnFilterSegmentSelected -> handleFilterSegmentSelected(intent.segmentId)
            is CategoryOffersIntent.OnOfferItemClicked -> handleOfferItemClicked(intent.offerId)
        }
    }
    
    private fun loadInitialData() = intent {
        reduce { 
            state.copy(
                filterSegments = getMockFilterSegments(),
                offers = getMockOffers(state.categoryType, state.selectedFilterSegmentId)
            )
        }
    }
    
    private fun handleBackClicked() = intent {
        postSideEffect(CategoryOffersSideEffect.NavigateBack)
    }
    
    private fun handleFilterSegmentSelected(segmentId: String) = intent {
        val newSelectedId = if (state.selectedFilterSegmentId == segmentId) null else segmentId
        reduce {
            state.copy(
                selectedFilterSegmentId = newSelectedId,
                offers = getMockOffers(state.categoryType, newSelectedId)
            )
        }
    }
    
    private fun handleOfferItemClicked(offerId: String) = intent {
        val offerItem = state.offers.find { it.id == offerId }
        if (offerItem != null) {
            postSideEffect(CategoryOffersSideEffect.NavigateToReserve(offerItem))
        }
    }
    
    
    // Mock data - replace with repository calls in real app
    private fun getMockFilterSegments(): List<FilterSegment> {
        return listOf(
            FilterSegment(
                id = "nearest",
                type = FilterSegmentType.NEAREST,
                text = "Nearest",
                icon = Res.drawable.ic_explore_24dp,
                iconTint = 0xFFFF8B38L // Orange color
            ),
            FilterSegment(
                id = "top_rated",
                type = FilterSegmentType.TOP_RATED,
                text = "Top rated",
                icon = Res.drawable.ic_star_16dp,
                iconTint = 0xFF5AA9E7L // Blue color
            ),
            FilterSegment(
                id = "hot_deals",
                type = FilterSegmentType.HOT_DEALS,
                text = "Hot deals",
                icon = Res.drawable.ic_mark_16dp,
                iconTint = 0xFFAD3CDAL // Purple color
            )
        )
    }
    
    private fun getMockOffers(categoryType: String, filterSegmentId: String? = null): List<OfferItem> {
        // Generate different offers based on filter segment
        val baseOffers = when (filterSegmentId) {
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
                id = "1",
                title = "Classic Burger",
                imageBgColor = "#fff2eb",
                originalPrice = "15.99",
                currentPrice = "12.99",
                restaurantName = "Burger House",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.8f,
                distance = "0.8 km"
            ),
            OfferItem(
                id = "2",
                title = "Cheese Burger",
                imageBgColor = "#fff2eb",
                originalPrice = "18.99",
                currentPrice = "14.99",
                restaurantName = "Burger King",
                pickupTime = "Pick up from 18:00 to 22:00",
                rating = 4.6f,
                distance = "1.2 km"
            ),
            OfferItem(
                id = "3",
                title = "Veggie Burger",
                imageBgColor = "#fff2eb",
                originalPrice = "13.99",
                currentPrice = "10.99",
                restaurantName = "Green Burger",
                pickupTime = "Pick up from 16:00 to 21:00",
                rating = 4.9f,
                distance = "2.1 km"
            ),
            OfferItem(
                id = "4",
                title = "BBQ Burger",
                imageBgColor = "#fff2eb",
                originalPrice = "19.99",
                currentPrice = "16.99",
                restaurantName = "BBQ Place",
                pickupTime = "Pick up from 17:30 to 23:30",
                rating = 4.7f,
                distance = "1.5 km"
            ),
            OfferItem(
                id = "5",
                title = "Double Burger",
                imageBgColor = "#fff2eb",
                originalPrice = "22.99",
                currentPrice = "18.99",
                restaurantName = "Mega Burger",
                pickupTime = "Pick up from 19:00 to 23:00",
                rating = 4.5f,
                distance = "0.9 km"
            )
        )
    }
    
    private fun getNearestOffers(): List<OfferItem> {
        return getAllOffers().sortedBy { it.distance.replace(" km", "").toFloatOrNull() ?: Float.MAX_VALUE }
    }
    
    private fun getTopRatedOffers(): List<OfferItem> {
        return getAllOffers().sortedByDescending { it.rating }
    }
    
    private fun getHotDealsOffers(): List<OfferItem> {
        return getAllOffers().sortedByDescending { 
            val original = it.originalPrice.toFloatOrNull() ?: 0f
            val current = it.currentPrice.toFloatOrNull() ?: 0f
            original - current // Sort by discount amount
        }
    }
}

