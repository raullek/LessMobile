package az.less.mobile.presentation.main.explore

import az.less.mobile.presentation.main.explore.models.FilterType

/**
 * State of the Explore Screen
 */
data class ExploreState(
    val searchQuery: String = "",
    val selectedFilterType: FilterType? = null,
    val isLoading: Boolean = false,
    val selectedVenueId: String? = null,
    val userLocation: az.less.mobile.presentation.maps.models.LatLong? = null,
    val locationPermissionGranted: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface ExploreSideEffect {
    data class NavigateToVenueDetail(val venueId: String) : ExploreSideEffect
    data object NavigateToSearch : ExploreSideEffect
    data object NavigateToFilter : ExploreSideEffect
    data class ShowError(val message: String) : ExploreSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface ExploreIntent {
    data class OnSearchQueryChanged(val query: String) : ExploreIntent
    data object OnSearchClicked : ExploreIntent
    data object OnFilterClicked : ExploreIntent
    data class OnFilterTypeSelected(val filterType: FilterType) : ExploreIntent
    data class OnMapMarkerClicked(val venueId: String) : ExploreIntent
    data class OnLocationPermissionChanged(val granted: Boolean) : ExploreIntent
    data class OnMapClick(val latLong: az.less.mobile.presentation.maps.models.LatLong) : ExploreIntent
}

