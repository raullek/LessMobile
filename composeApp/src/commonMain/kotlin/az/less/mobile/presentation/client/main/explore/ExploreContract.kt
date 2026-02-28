package az.less.mobile.presentation.client.main.explore

import az.less.mobile.domain.model.SearchBox
import az.less.mobile.domain.model.SearchVenue
import az.less.mobile.presentation.client.main.explore.models.ExploreVenueItem
import az.less.mobile.presentation.client.main.explore.models.FilterData
import az.less.mobile.presentation.client.main.explore.models.FilterItem
import az.less.mobile.presentation.client.main.explore.models.FilterType
import az.less.mobile.presentation.client.main.explore.models.OfferSlot
import az.less.mobile.presentation.client.main.explore.models.QuickFilter
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.Marker

/**
 * State of the Explore Screen
 */
data class ExploreState(
    val searchQuery: String = "",
    val selectedFilterType: FilterType? = null,
    val isLoading: Boolean = false,
    val selectedVenueId: String? = null,
    val selectedMerchantName: String = "",
    val selectedMerchantSlots: List<OfferSlot> = emptyList(),
    val userLocation: LatLong? = null,
    val locationPermissionGranted: Boolean = false,
    val filterItems: List<FilterItem> = emptyList(),
    val markers: List<Marker> = emptyList(),
    val isFilterSheetVisible: Boolean = false,
    val filterData: FilterData = FilterData(),
    val selectedMarkerPosition: LatLong? = null,
    // API data
    val venues: List<SearchVenue> = emptyList(),
    val boxes: List<SearchBox> = emptyList(),
    val totalResults: Int = 0,
    val error: String? = null,
    // Active filter tracking
    val activeQuickFilters: Set<QuickFilter> = emptySet(),
    val activeFilters: Map<String, List<String>> = emptyMap()
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface ExploreSideEffect {
    data class NavigateToVenueDetail(val venueId: String) :
        ExploreSideEffect
    data object NavigateToSearch : ExploreSideEffect
    data object NavigateToFilter : ExploreSideEffect
    data class ShowError(val message: String) :
        ExploreSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface ExploreIntent {
    data class OnSearchQueryChanged(val query: String) :
        ExploreIntent
    data object OnSearchClicked : ExploreIntent
    data object OnFilterClicked : ExploreIntent
    data class OnFilterTypeSelected(val filterType: FilterType) :
        ExploreIntent
    data class OnFilterItemClicked(val quickFilter: QuickFilter) :
        ExploreIntent
    data class OnMapMarkerClicked(val venueId: String) :
        ExploreIntent
    data class OnLocationPermissionChanged(val granted: Boolean) :
        ExploreIntent
    data class OnVenueItemClicked(val venueId: String) :
        ExploreIntent
    data object OnFilterSheetDismissed :
        ExploreIntent
    data class OnFilterOptionClicked(val categoryId: String, val optionId: String) :
        ExploreIntent
    data object OnApplyFilters : ExploreIntent
    data object OnDidCenterCameraOnMarker : ExploreIntent
}

