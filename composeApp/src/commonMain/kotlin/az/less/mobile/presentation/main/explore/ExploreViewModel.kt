package az.less.mobile.presentation.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}

