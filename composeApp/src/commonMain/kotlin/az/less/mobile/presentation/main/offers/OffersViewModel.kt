package az.less.mobile.presentation.main.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.offers.models.Category
import az.less.mobile.presentation.main.offers.models.OfferItem
import az.less.mobile.presentation.main.offers.models.OfferSection
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.test_offer_category_burger
import lessmobile.composeapp.generated.resources.test_offer_category_pasta
import lessmobile.composeapp.generated.resources.test_offer_category_pizza
import lessmobile.composeapp.generated.resources.test_offer_category_sushi
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
            is OffersIntent.OnOfferItemClicked -> handleOfferItemClicked(intent.offerId)
        }
    }
    
    private fun loadInitialData() = intent {
        reduce { 
            state.copy(
                categories = getMockCategories(),
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
            Category(id = "4", title = "Italian", testImage = Res.drawable.test_offer_category_pasta)
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

