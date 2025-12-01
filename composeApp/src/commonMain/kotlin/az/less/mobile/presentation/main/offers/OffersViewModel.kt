package az.less.mobile.presentation.main.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.offers.models.Category
import az.less.mobile.presentation.main.offers.models.FilterSegment
import az.less.mobile.presentation.main.offers.models.FilterSegmentType
import az.less.mobile.presentation.main.offers.models.OfferItem
import az.less.mobile.presentation.main.offers.models.OfferSection
import az.less.mobile.presentation.main.offers.models.SpecialDiscountItem
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_mark_16dp
import lessmobile.composeapp.generated.resources.ic_star_16dp
import lessmobile.composeapp.generated.resources.test_offer_category_burger
import lessmobile.composeapp.generated.resources.test_offer_category_pasta
import lessmobile.composeapp.generated.resources.test_offer_category_pizza
import lessmobile.composeapp.generated.resources.test_offer_category_sushi
import lessmobile.composeapp.generated.resources.test_offer_item_image
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container


/**
 * ViewModel for Offers Screen using Orbit MVI
 */
class OffersViewModel : ViewModel(), ContainerHost<OffersState, OffersSideEffect> {
    
    override val container: Container<OffersState, OffersSideEffect> = 
        viewModelScope.container(OffersState())
    
    init {
        loadInitialData()
    }
    
    /**
     * Handle user intents
     */
    fun onIntent(intent: OffersIntent) {
        when (intent) {
            is OffersIntent.OnSearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is OffersIntent.OnSearchClicked -> handleSearchClicked()
            is OffersIntent.OnCategorySelected -> handleCategorySelected(intent.categoryId)
            is OffersIntent.OnFilterSegmentSelected -> handleFilterSegmentSelected(intent.segmentId)
            is OffersIntent.OnOfferItemClicked -> handleOfferItemClicked(intent.offerId)
        }
    }
    
    private fun loadInitialData() = intent {
        reduce { 
            state.copy(
                categories = getMockCategories(),
                specialDiscounts = getMockSpecialDiscounts(),
                filterSegments = getMockFilterSegments(),
                offerSections = getMockOfferSections()
            )
        }
    }
    
    private fun handleSearchQueryChanged(query: String) = intent {
        reduce { 
            state.copy(searchQuery = query)
        }
    }
    
    private fun handleSearchClicked() = intent {
        postSideEffect(OffersSideEffect.NavigateToSearch)
    }
    
    private fun handleCategorySelected(categoryId: String) = intent {
        reduce {
            state.copy(
                selectedCategoryId = if (state.selectedCategoryId == categoryId) null else categoryId
            )
        }
        // In real app, load filtered items based on category
    }
    
    private fun handleFilterSegmentSelected(segmentId: String) = intent {
        reduce {
            state.copy(
                selectedFilterSegmentId = if (state.selectedFilterSegmentId == segmentId) null else segmentId
            )
        }
        // In real app, load filtered items based on selected filter segment
        // You can use state.selectedFilterSegmentId to determine which filter is active
    }
    
    private fun handleOfferItemClicked(offerId: String) = intent {
        // Navigate to Reserve screen when clicking on offer item
        postSideEffect(OffersSideEffect.NavigateToReserve)
    }
    
    // Mock data - replace with repository calls in real app
    private fun getMockCategories(): List<Category> {
        return listOf(
            Category(id = "1", title = "Burger", testImage = Res.drawable.test_offer_category_burger),
            Category(id = "2", title = "Pizza", testImage = Res.drawable.test_offer_category_pizza),
            Category(id = "3", title = "Asian", testImage = Res.drawable.test_offer_category_sushi),
            Category(id = "4", title = "Italian", testImage = Res.drawable.test_offer_category_pasta),
            Category(id = "5", title = "Bakery", testImage = Res.drawable.test_offer_category_pasta),
            Category(id = "6", title = "Italian", testImage = Res.drawable.test_offer_category_pasta)
        )
    }
    
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
    
    private fun getMockSpecialDiscounts(): List<SpecialDiscountItem> {
        return listOf(
            SpecialDiscountItem(
                id = "1",
                title = "Special discount for Desserts 🧁",
                subtitle = "Hurry to pick up from 22:00",
                testImage = Res.drawable.test_offer_item_image
            ),
            SpecialDiscountItem(
                id = "2",
                title = "Special discount for Pizza 🍕",
                subtitle = "Hurry to pick up from 20:00",
                testImage = Res.drawable.test_offer_item_image
            ),
            SpecialDiscountItem(
                id = "3",
                title = "Special discount for Burgers 🍔",
                subtitle = "Hurry to pick up from 19:00",
                testImage = Res.drawable.test_offer_item_image
            ),
            SpecialDiscountItem(
                id = "4",
                title = "Special discount for Sushi 🍣",
                subtitle = "Hurry to pick up from 21:00",
                testImage = Res.drawable.test_offer_item_image
            ),
            SpecialDiscountItem(
                id = "5",
                title = "Special discount for Pasta 🍝",
                subtitle = "Hurry to pick up from 18:00",
                testImage = Res.drawable.test_offer_item_image
            )
        )
    }
    
    private fun getMockOfferSections(): List<OfferSection> {
        val topRatedItems = listOf(
            OfferItem(
                id = "1",
                title = "Belgian Coffee",
                imageBgColor = "#fff2eb",
                originalPrice = "12.99",
                currentPrice = "12.99",
                restaurantName = "Small Surprise Bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                distance = "1.2 km"
            ),
            OfferItem(
                id = "2",
                title = "Belgian Coffee",
                imageBgColor = "#fff2eb",
                originalPrice = "12.99",
                currentPrice = "12.99",
                restaurantName = "Small Surprise Bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                distance = "1.2 km"
            ),
            OfferItem(
                id = "3",
                title = "Belgian Coffee",
                imageBgColor = "#fff2eb",
                originalPrice = "12.99",
                currentPrice = "12.99",
                restaurantName = "Small Surprise Bag",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                distance = "1.2 km"
            )
        )
        
        return listOf(
            OfferSection(
                id = "top_rated",
                title = "Top rated",
                items = topRatedItems,
                showSeeAll = true
            ),
            OfferSection(
                id = "top_picks",
                title = "Top picks for late dinner",
                items = topRatedItems,
                showSeeAll = true
            )
        )
    }
}

