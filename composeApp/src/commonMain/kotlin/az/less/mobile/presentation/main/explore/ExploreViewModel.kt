package az.less.mobile.presentation.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.explore.models.ExploreVenueItem
import az.less.mobile.presentation.main.explore.models.FilterType
import az.less.mobile.presentation.maps.models.LatLong
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Explore Screen using Orbit MVI
 * Map exploration with search and filter chips
 */
class ExploreViewModel : ViewModel(), ContainerHost<ExploreState, ExploreSideEffect> {
    
    override val container: Container<ExploreState, ExploreSideEffect> = 
        viewModelScope.container(ExploreState())
    
    init {
        loadVenues()
    }
    
    /**
     * Handle user intents
     */
    fun onIntent(intent: ExploreIntent) {
        when (intent) {
            is ExploreIntent.OnSearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is ExploreIntent.OnSearchClicked -> handleSearchClicked()
            is ExploreIntent.OnFilterClicked -> handleFilterClicked()
            is ExploreIntent.OnFilterTypeSelected -> handleFilterTypeSelected(intent.filterType)
            is ExploreIntent.OnMapMarkerClicked -> handleMapMarkerClicked(intent.venueId)
            is ExploreIntent.OnLocationPermissionChanged -> handleLocationPermissionChanged(intent.granted)
            is ExploreIntent.OnMapClick -> handleMapClick(intent.latLong)
            is ExploreIntent.OnVenueItemClicked -> handleVenueItemClicked(intent.venueId)
        }
    }
    
    private fun loadVenues() = intent {
        reduce {
            state.copy(
                venues = getMockVenues(),
                isLoading = false
            )
        }
    }
    
    private fun handleSearchQueryChanged(query: String) = intent {
        reduce { 
            state.copy(searchQuery = query)
        }
    }
    
    private fun handleSearchClicked() = intent {
        postSideEffect(ExploreSideEffect.NavigateToSearch)
    }
    
    private fun handleFilterClicked() = intent {
        postSideEffect(ExploreSideEffect.NavigateToFilter)
    }
    
    private fun handleFilterTypeSelected(filterType: FilterType) = intent {
        reduce {
            state.copy(
                selectedFilterType = if (state.selectedFilterType == filterType) null else filterType
            )
        }
        // In real app, filter venues based on selected filter
    }
    
    private fun handleMapMarkerClicked(venueId: String) = intent {
        reduce {
            state.copy(selectedVenueId = venueId)
        }
        // Navigate to venue detail
        postSideEffect(ExploreSideEffect.NavigateToVenueDetail(venueId))
    }
    
    private fun handleLocationPermissionChanged(granted: Boolean) = intent {
        reduce {
            state.copy(locationPermissionGranted = granted)
        }
    }
    
    private fun handleMapClick(latLong: LatLong) = intent {
        reduce {
            state.copy(selectedVenueId = null)
        }
    }
    
    private fun handleVenueItemClicked(venueId: String) = intent {
        postSideEffect(ExploreSideEffect.NavigateToVenueDetail(venueId))
    }
    
    // Mock data - replace with repository calls in real app
    private fun getMockVenues(): List<ExploreVenueItem> {
        return listOf(
            ExploreVenueItem(
                id = "1",
                title = "Mixed donut bag",
                price = "12.99",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                reviewCount = "28+",
                distance = "1.2 km away"
            ),
            ExploreVenueItem(
                id = "2",
                title = "Fresh sandwich pack",
                price = "15.50",
                pickupTime = "Pick up from 12:00 to 20:00",
                rating = 4.8f,
                reviewCount = "42+",
                distance = "0.8 km away"
            ),
            ExploreVenueItem(
                id = "3",
                title = "Coffee & pastries",
                price = "8.99",
                pickupTime = "Pick up from 08:00 to 18:00",
                rating = 4.6f,
                reviewCount = "35+",
                distance = "1.5 km away"
            ),
            ExploreVenueItem(
                id = "4",
                title = "Bakery special box",
                price = "18.00",
                pickupTime = "Pick up from 10:00 to 22:00",
                rating = 4.7f,
                reviewCount = "19+",
                distance = "2.1 km away"
            ),
            ExploreVenueItem(
                id = "5",
                title = "Grocery surprise bag",
                price = "22.50",
                pickupTime = "Pick up from 09:00 to 21:00",
                rating = 4.5f,
                reviewCount = "31+",
                distance = "0.5 km away"
            )
        )
    }
}

