package az.less.mobile.presentation.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.search.models.SearchCategory
import az.less.mobile.presentation.main.search.models.SearchOffer
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.test_offer_category_burger
import lessmobile.composeapp.generated.resources.test_offer_category_pasta
import lessmobile.composeapp.generated.resources.test_offer_category_pizza
import lessmobile.composeapp.generated.resources.test_offer_category_sushi
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Search Screen using Orbit MVI
 */
@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel(), ContainerHost<SearchState, SearchSideEffect> {
    
    override val container: Container<SearchState, SearchSideEffect> = 
        viewModelScope.container(SearchState())
    
    private val searchQueryFlow = MutableStateFlow("")
    
    init {
        loadInitialData()
        setupSearchFlow()
    }
    
    /**
     * Setup search flow with debounce and distinctUntilChanged
     */
    private fun setupSearchFlow() {
        searchQueryFlow
            .debounce(500L) // 500ms delay after user stops typing
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    // Clear offers when search query is empty
                    intent {
                        reduce { state.copy(offers = emptyList(), isLoading = false) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * Handle user intents
     */
    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.OnSearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is SearchIntent.OnCategorySelected -> handleCategorySelected(intent.categoryId)
            is SearchIntent.OnOfferClicked -> handleOfferClicked(intent.offerId)
            is SearchIntent.OnBackClicked -> handleBackClicked()
            is SearchIntent.OnMapClicked -> handleOnMapClicked()
        }
    }
    
    private fun loadInitialData() = intent {
        reduce { 
            state.copy(
                categories = getMockCategories()
            )
        }
    }
    
    private fun handleSearchQueryChanged(query: String) = intent {
        reduce { 
            state.copy(searchQuery = query, isLoading = query.isNotEmpty())
        }
        // Emit to search flow which will handle debouncing
        searchQueryFlow.value = query
    }
    
    /**
     * Perform search with mock delay to simulate API call
     */
    private fun performSearch(query: String) = intent {
        reduce { state.copy(isLoading = true) }
        
        // Simulate API delay
        delay(300L)
        
        // Mock search - filter offers by query
        val searchResults = getMockOffers().filter { offer ->
            offer.title.contains(query, ignoreCase = true) ||
            offer.pickupTime.contains(query, ignoreCase = true)
        }
        
        reduce {
            state.copy(
                offers = searchResults,
                isLoading = false
            )
        }
    }
    
    private fun handleCategorySelected(categoryId: String) = intent {
        postSideEffect(SearchSideEffect.NavigateToCategory(categoryId))
    }
    
    private fun handleBackClicked() = intent {
        postSideEffect(SearchSideEffect.NavigateBack)
    }
    
    private fun handleOnMapClicked() = intent {
        postSideEffect(SearchSideEffect.NavigateToMap)
        // In real app, navigate to filter screen
    }
    
    private fun handleOfferClicked(offerId: String) = intent {
        // In real app, navigate to offer detail screen
        // postSideEffect(SearchSideEffect.NavigateToOffer(offerId))
    }
    
    // Mock data - replace with repository calls in real app
    private fun getMockCategories(): List<SearchCategory> {
        return listOf(
            SearchCategory(id = "meals", title = "Meals", testImage = Res.drawable.test_offer_category_burger),
            SearchCategory(id = "lunch", title = "Lunch", testImage = Res.drawable.test_offer_category_pizza),
            SearchCategory(id = "dinner", title = "Dinner", testImage = Res.drawable.test_offer_category_sushi),
            SearchCategory(id = "bakery", title = "Bakery", testImage = Res.drawable.test_offer_category_pasta),
            SearchCategory(id = "dessert", title = "Dessert", testImage = Res.drawable.test_offer_category_burger),
            SearchCategory(id = "grocery", title = "Grocery", testImage = Res.drawable.test_offer_category_pizza),
            SearchCategory(id = "healthy", title = "Healthy", testImage = Res.drawable.test_offer_category_sushi)
        )
    }
    
    /**
     * Mock offers data for search results
     */
    private fun getMockOffers(): List<SearchOffer> {
        return listOf(
            SearchOffer(
                id = "1",
                title = "Belgian Waffle Breakfast Box",
                price = "12.99",
                pickupTime = "Pick up from 08:00 to 12:00",
                rating = 4.5f,
                reviewCount = "120+",
                distance = "0.8 km away"
            ),
            SearchOffer(
                id = "2",
                title = "Belgian Chocolate Surprise",
                price = "8.50",
                pickupTime = "Pick up from 14:00 to 20:00",
                rating = 4.7f,
                reviewCount = "85+",
                distance = "1.2 km away"
            ),
            SearchOffer(
                id = "3",
                title = "Fresh Bakery Box",
                price = "6.99",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.3f,
                reviewCount = "45+",
                distance = "2.1 km away"
            ),
            SearchOffer(
                id = "4",
                title = "Gourmet Pizza Deal",
                price = "15.99",
                pickupTime = "Pick up from 12:00 to 22:00",
                rating = 4.8f,
                reviewCount = "200+",
                distance = "0.5 km away"
            ),
            SearchOffer(
                id = "5",
                title = "Sushi Combo Box",
                price = "18.50",
                pickupTime = "Pick up from 11:00 to 21:00",
                rating = 4.6f,
                reviewCount = "150+",
                distance = "1.5 km away"
            ),
            SearchOffer(
                id = "6",
                title = "Healthy Lunch Box",
                price = "10.99",
                pickupTime = "Pick up from 10:00 to 16:00",
                rating = 4.4f,
                reviewCount = "95+",
                distance = "1.8 km away"
            )
        )
    }
}


