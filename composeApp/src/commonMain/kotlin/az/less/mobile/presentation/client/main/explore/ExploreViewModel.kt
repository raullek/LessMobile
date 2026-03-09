package az.less.mobile.presentation.client.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.remote.model.SearchFilterDto
import az.less.mobile.data.remote.model.SearchFilterParam
import az.less.mobile.domain.repository.ExploreRepository
import az.less.mobile.presentation.client.main.explore.models.FilterCategory
import az.less.mobile.presentation.client.main.explore.models.FilterData
import az.less.mobile.presentation.client.main.explore.models.FilterItem
import az.less.mobile.presentation.client.main.explore.models.FilterOption
import az.less.mobile.presentation.client.main.explore.models.OfferSlot
import az.less.mobile.presentation.client.main.explore.models.QuickFilter
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.Marker
import az.less.mobile.utils.formatPrice
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class ExploreViewModel(
    private val exploreRepository: ExploreRepository
) : ViewModel(), ContainerHost<ExploreState, ExploreSideEffect> {

    override val container: Container<ExploreState, ExploreSideEffect> =
        viewModelScope.container(ExploreState())

    private val geolocator: Geolocator = Geolocator.mobile()

    init {
        loadFilterItems()
        loadFilterData()
        performSearch()
    }

    fun onIntent(intent: ExploreIntent) {
        when (intent) {
            is ExploreIntent.OnSearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is ExploreIntent.OnSearchClicked -> handleSearchClicked()
            is ExploreIntent.OnFilterClicked -> handleFilterClicked()
            is ExploreIntent.OnFilterTypeSelected -> { /* handled via QuickFilter.FAVORITE */ }
            is ExploreIntent.OnFilterItemClicked -> handleFilterItemClicked(intent.quickFilter)
            is ExploreIntent.OnMapMarkerClicked -> handleMapMarkerClicked(intent.venueId)
            is ExploreIntent.OnLocationPermissionChanged -> handleLocationPermissionChanged(intent.granted)
            is ExploreIntent.OnVenueItemClicked -> handleVenueItemClicked(intent.venueId)
            is ExploreIntent.OnFilterSheetDismissed -> handleFilterSheetDismissed()
            is ExploreIntent.OnFilterOptionClicked -> handleFilterOptionClicked(intent.categoryId, intent.optionId)
            is ExploreIntent.OnApplyFilters -> handleApplyFilters()
            is ExploreIntent.OnDidCenterCameraOnMarker -> handleDidCenterCameraOnMarker()
            is ExploreIntent.OnDismissMerchantSlots -> handleDismissMerchantSlots()
            is ExploreIntent.OnSlotClicked -> handleSlotClicked(intent.slotId)
        }
    }

    private suspend fun getLocation() = withTimeoutOrNull(3000L) {
        if (geolocator.isAvailable()) geolocator.current().getOrNull() else null
    }

    private fun buildFiltersJson(): String {
        val filterParams = container.stateFlow.value.filterData.categories
            .mapNotNull { category ->
                val selectedValues = category.options
                    .filter { it.isSelected }
                    .map { it.id }
                if (selectedValues.isNotEmpty()) {
                    SearchFilterParam(
                        searchFilterId = category.id,
                        values = selectedValues
                    )
                } else null
            }
        return Json.encodeToString(filterParams)
    }

    private fun performSearch() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        val location = if (state.locationPermissionGranted) getLocation() else null
        val latitude = location?.coordinates?.latitude
        val longitude = location?.coordinates?.longitude

        val quickFilters = state.activeQuickFilters
        val isFavorite = if (QuickFilter.FAVORITE in quickFilters) true else null
        val openNow = if (QuickFilter.OPEN_NOW in quickFilters) true else null
        val nearest = if (QuickFilter.NEAREST in quickFilters) true else null
        val hotDeals = if (QuickFilter.HOT_DEALS in quickFilters) true else null

        val filtersJson = buildFiltersJson()

        exploreRepository.searchVenuesByFilters(
            filters = filtersJson,
            latitude = latitude,
            longitude = longitude,
            isFavorite = isFavorite,
            nearest = nearest,
            hotDeals = hotDeals,
            openNow = openNow
        )
            .onSuccess { data ->
                val markers = data.venues.map { venue ->
                    Marker(
                        id = "marker_${venue.id}",
                        position = LatLong(venue.latitude, venue.longitude),
                        title = venue.name,
                        snippet = "${venue.activeBoxes} boxes available",
                        iconUrl = venue.logo,
                        tag = venue.id,
                        itemsCount = venue.activeBoxes
                    )
                }

                reduce {
                    state.copy(
                        isLoading = false,
                        venues = data.venues,
                        totalResults = data.total,
                        markers = markers,
                        error = null
                    )
                }
            }
            .onError { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
                postSideEffect(ExploreSideEffect.ShowError(error.message))
            }
    }

    private fun loadFilterItems() = intent {
        val filterItems = QuickFilter.entries.map { qf ->
            FilterItem(quickFilter = qf)
        }
        reduce { state.copy(filterItems = filterItems) }
    }

    private fun handleSearchQueryChanged(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
    }

    private fun handleSearchClicked() = intent {
        postSideEffect(ExploreSideEffect.NavigateToSearch)
    }

    private fun handleFilterClicked() = intent {
        reduce { state.copy(isFilterSheetVisible = true) }
    }

    private fun handleFilterItemClicked(quickFilter: QuickFilter) = intent {
        val isActive = quickFilter in state.activeQuickFilters

        // Nearest and HotDeals are mutually exclusive sort options
        val mutuallyExclusive = setOf(QuickFilter.NEAREST, QuickFilter.HOT_DEALS)

        val updatedQuickFilters = if (isActive) {
            state.activeQuickFilters - quickFilter
        } else {
            val base = if (quickFilter in mutuallyExclusive) {
                state.activeQuickFilters - mutuallyExclusive
            } else {
                state.activeQuickFilters
            }
            base + quickFilter
        }

        val updatedFilterItems = state.filterItems.map { item ->
            item.copy(isSelected = item.quickFilter in updatedQuickFilters)
        }

        reduce {
            state.copy(
                activeQuickFilters = updatedQuickFilters,
                filterItems = updatedFilterItems
            )
        }
        performSearch()
    }

    private fun handleMapMarkerClicked(venueId: String) = intent {
        val venue = state.venues.find { it.id == venueId }
        val merchantName = venue?.name ?: ""

        val clickedMarker = state.markers.find { marker ->
            val markerVenueId = marker.tag as? String ?: marker.id
            markerVenueId == venueId
        }

        val adjustedPosition = clickedMarker?.position?.let { position ->
            LatLong(
                latitude = position.latitude,
                longitude = position.longitude
            )
        }

        reduce {
            val updatedMarkers = state.markers.map { marker ->
                val markerVenueId = marker.tag as? String ?: marker.id
                marker.copy(isSelected = markerVenueId == venueId)
            }
            state.copy(
                markers = updatedMarkers,
                selectedVenueId = venueId,
                selectedMerchantName = merchantName,
                selectedMerchantSlots = emptyList(),
                selectedMarkerPosition = adjustedPosition
            )
        }

        // Fetch boxes for this venue
        exploreRepository.getBoxesForVenue(venueId)
            .onSuccess { data ->
                val slots = data.boxes.map { box ->
                    OfferSlot(
                        id = box.id,
                        title = box.title,
                        price = box.discountedPrice.formatPrice(),
                        pickupTime = "",
                        imageUrl = box.images.firstOrNull()
                    )
                }
                reduce { state.copy(selectedMerchantSlots = slots) }
            }
            .onError { error ->
                postSideEffect(ExploreSideEffect.ShowError(error.message))
            }
    }

    private fun handleLocationPermissionChanged(granted: Boolean) = intent {
        reduce { state.copy(locationPermissionGranted = granted) }
        if (granted) {
            performSearch()
        }
    }

    private fun handleVenueItemClicked(venueId: String) = intent {
        postSideEffect(ExploreSideEffect.NavigateToVenueDetail(venueId))
    }

    private fun handleFilterSheetDismissed() = intent {
        reduce { state.copy(isFilterSheetVisible = false) }
    }

    private fun handleFilterOptionClicked(categoryId: String, optionId: String) = intent {
        val updatedFilterData = state.filterData.copy(
            categories = state.filterData.categories.map { category ->
                if (category.id == categoryId) {
                    val isCurrentlySelected = category.options
                        .find { it.id == optionId }?.isSelected ?: false
                    category.copy(
                        options = category.options.map { option ->
                            when {
                                option.id == optionId -> option.copy(isSelected = !isCurrentlySelected)
                                // Single-select: deselect others in this category
                                !category.isMultiSelect -> option.copy(isSelected = false)
                                else -> option
                            }
                        }
                    )
                } else {
                    category
                }
            }
        )
        reduce { state.copy(filterData = updatedFilterData) }
    }

    private fun handleApplyFilters() = intent {
        val hasActiveFilters = state.filterData.categories.any { category ->
            category.options.any { it.isSelected }
        }

        val updatedFilterItems = state.filterItems.map { item ->
            if (item.quickFilter == QuickFilter.FILTER_BUTTON) item.copy(isSelected = hasActiveFilters)
            else item
        }

        reduce {
            state.copy(
                isFilterSheetVisible = false,
                filterItems = updatedFilterItems
            )
        }

        performSearch()
    }

    private fun handleDidCenterCameraOnMarker() = intent {
        reduce { state.copy(selectedMarkerPosition = null) }
    }

    private fun handleDismissMerchantSlots() = intent {
        val updatedMarkers = state.markers.map { it.copy(isSelected = false) }
        reduce {
            state.copy(
                selectedVenueId = null,
                selectedMerchantName = "",
                selectedMerchantSlots = emptyList(),
                markers = updatedMarkers
            )
        }
    }

    private fun handleSlotClicked(slotId: String) = intent {
        postSideEffect(ExploreSideEffect.NavigateToReserve(slotId))
    }

    private fun loadFilterData() = intent {
        exploreRepository.getSearchFilters()
            .onSuccess { filters ->
                val categories = filters.map { it.toFilterCategory() }
                reduce { state.copy(filterData = FilterData(categories = categories)) }
            }
            .onError { error ->
                postSideEffect(ExploreSideEffect.ShowError(error.message))
            }
    }

    private fun SearchFilterDto.toFilterCategory(): FilterCategory {
        return FilterCategory(
            id = id,
            title = title,
            fieldName = fieldName,
            isMultiSelect = isMultiSelect,
            options = options.map { option ->
                FilterOption(
                    id = option.value,
                    text = option.key,
                    imageUrl = option.imageUrl
                )
            }
        )
    }
}
