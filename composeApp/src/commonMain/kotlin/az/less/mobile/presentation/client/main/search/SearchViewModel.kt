package az.less.mobile.presentation.client.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.less.mobile.domain.model.SearchVenue
import az.less.mobile.domain.repository.ExploreRepository
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

}

private data class LocationParams(
    val latitude: Double? = null,
    val longitude: Double? = null
)
