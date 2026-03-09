package az.less.mobile.presentation.client.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.less.mobile.domain.model.SearchVenue
import az.less.mobile.domain.repository.ExploreRepository
import az.less.mobile.presentation.client.main.search.models.SearchCategory
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.test_offer_category_burger
import lessmobile.composeapp.generated.resources.test_offer_category_pasta
import lessmobile.composeapp.generated.resources.test_offer_category_pizza
import lessmobile.composeapp.generated.resources.test_offer_category_sushi
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val exploreRepository: ExploreRepository
) : ViewModel(), ContainerHost<SearchState, SearchSideEffect> {

    override val container: Container<SearchState, SearchSideEffect> =
        viewModelScope.container(SearchState())

    private val geolocator: Geolocator = Geolocator.mobile()

    private val searchQueryFlow = MutableStateFlow("")
    private val locationFlow = MutableStateFlow<LocationParams?>(null)

    val searchResults: Flow<PagingData<SearchVenue>> = searchQueryFlow
        .debounce(500L)
        .distinctUntilChanged()
        .combine(locationFlow) { query, location -> Pair(query, location) }
        .flatMapLatest { (query, location) ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                exploreRepository.searchVenues(
                    query = query,
                    latitude = location?.latitude,
                    longitude = location?.longitude
                )
            }
        }
        .cachedIn(viewModelScope)

    init {
        loadInitialData()
        fetchLocation()
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.OnSearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is SearchIntent.OnCategorySelected -> handleCategorySelected(intent.categoryId)
            is SearchIntent.OnVenueClicked -> handleVenueClicked(intent.venueId)
            is SearchIntent.OnBackClicked -> handleBackClicked()
            is SearchIntent.OnMapClicked -> handleOnMapClicked()
        }
    }

    private fun loadInitialData() = intent {
        reduce {
            state.copy(categories = getMockCategories())
        }
    }

    private fun fetchLocation() {
        viewModelScope.launch {
            val location = withTimeoutOrNull(3000L) {
                if (geolocator.isAvailable()) geolocator.current().getOrNull() else null
            }
            locationFlow.value = LocationParams(
                latitude = location?.coordinates?.latitude,
                longitude = location?.coordinates?.longitude
            )
        }
    }

    private fun handleSearchQueryChanged(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    private fun handleCategorySelected(categoryId: String) = intent {
        val category = state.categories.find { it.id == categoryId }
        if (category != null) {
            postSideEffect(
                SearchSideEffect.NavigateToCategoryOffers(
                    categoryId = category.id,
                    categoryType = category.type,
                    categoryTitle = category.title
                )
            )
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(SearchSideEffect.NavigateBack)
    }

    private fun handleOnMapClicked() = intent {
        postSideEffect(SearchSideEffect.NavigateToMap)
    }

    private fun handleVenueClicked(venueId: String) = intent {
        postSideEffect(SearchSideEffect.NavigateToMerchantProfile(venueId))
    }

    private fun getMockCategories(): List<SearchCategory> {
        return listOf(
            SearchCategory(
                id = "meals",
                type = "SEARCH_CATEGORY",
                title = "Meals",
                testImage = Res.drawable.test_offer_category_burger
            ),
            SearchCategory(
                id = "lunch",
                type = "SEARCH_CATEGORY",
                title = "Lunch",
                testImage = Res.drawable.test_offer_category_pizza
            ),
            SearchCategory(
                id = "dinner",
                type = "SEARCH_CATEGORY",
                title = "Dinner",
                testImage = Res.drawable.test_offer_category_sushi
            ),
            SearchCategory(
                id = "bakery",
                type = "SEARCH_CATEGORY",
                title = "Bakery",
                testImage = Res.drawable.test_offer_category_pasta
            ),
            SearchCategory(
                id = "dessert",
                type = "SEARCH_CATEGORY",
                title = "Dessert",
                testImage = Res.drawable.test_offer_category_burger
            ),
            SearchCategory(
                id = "grocery",
                type = "SEARCH_CATEGORY",
                title = "Grocery",
                testImage = Res.drawable.test_offer_category_pizza
            ),
            SearchCategory(
                id = "healthy",
                type = "SEARCH_CATEGORY",
                title = "Healthy",
                testImage = Res.drawable.test_offer_category_sushi
            )
        )
    }
}

private data class LocationParams(
    val latitude: Double? = null,
    val longitude: Double? = null
)
