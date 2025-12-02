package az.less.mobile.presentation.main.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.explore.models.ExploreVenueItem
import az.less.mobile.presentation.main.explore.models.FilterCategory
import az.less.mobile.presentation.main.explore.models.FilterData
import az.less.mobile.presentation.main.explore.models.FilterIconType
import az.less.mobile.presentation.main.explore.models.FilterItem
import az.less.mobile.presentation.main.explore.models.FilterOption
import az.less.mobile.presentation.main.explore.models.FilterType
import az.less.mobile.presentation.main.explore.models.MerchantOffer
import az.less.mobile.presentation.main.explore.models.OfferSlot
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.Marker
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
        loadFilterItems()
        loadMerchantOffers()
        loadFilterData()
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
            is ExploreIntent.OnFilterItemClicked -> handleFilterItemClicked(intent.filterId)
            is ExploreIntent.OnMapMarkerClicked -> handleMapMarkerClicked(intent.venueId)
            is ExploreIntent.OnLocationPermissionChanged -> handleLocationPermissionChanged(intent.granted)
            is ExploreIntent.OnMapClick -> handleMapClick(intent.latLong)
            is ExploreIntent.OnVenueItemClicked -> handleVenueItemClicked(intent.venueId)
            is ExploreIntent.OnFilterSheetDismissed -> handleFilterSheetDismissed()
            is ExploreIntent.OnFilterOptionClicked -> handleFilterOptionClicked(intent.categoryId, intent.optionId)
            is ExploreIntent.OnApplyFilters -> handleApplyFilters()
        }
    }

    
    private fun loadMerchantOffers() = intent {
        // Mock: Fetch merchant offers from backend
        val merchantOffers = getMockMerchantOffers()
        
        // Parse merchant offers to markers
        val markers = merchantOffers.map { offer ->
            Marker(
                id = "marker_${offer.merchantId}",
                position = offer.coordinates,
                title = offer.merchantName,
                snippet = "${offer.slots.size} slots available",
                iconUrl = offer.merchantLogoUrl ?: "test_merchant_logo",
                tag = offer.merchantId, // Store merchant ID in tag
                itemsCount = offer.slots.size // Slot count for badge
            )
        }
        
        reduce {
            state.copy(
                markers = markers
            )
        }
    }
    
    private fun loadFilterItems() = intent {
        val dynamicFilters = getMockDynamicFilters()
        
        // Create unified filter items list: Filter button, Liked, then dynamic filters
        val filterItems = buildList {
            // Filter button (always first)
            add(
                FilterItem(
                    id = "filter_button",
                    text = null,
                    iconType = FilterIconType.FILTER,
                    isSelected = false
                )
            )
            
            // Liked button (always second)
            add(
                FilterItem(
                    id = "liked",
                    text = null,
                    iconType = FilterIconType.HEART,
                    isSelected = state.selectedFilterType == FilterType.LIKED
                )
            )
            
            // Dynamic filters from backend
            dynamicFilters.forEach { (id, displayName) ->
                add(
                    FilterItem(
                        id = id,
                        text = displayName,
                        iconType = FilterIconType.NONE,
                        isSelected = false
                    )
                )
            }
        }
        
        reduce {
            state.copy(filterItems = filterItems)
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
        reduce {
            state.copy(isFilterSheetVisible = true)
        }
    }
    
    private fun handleFilterTypeSelected(filterType: FilterType) = intent {
        val newSelectedFilterType = if (state.selectedFilterType == filterType) null else filterType
        reduce {
            state.copy(
                selectedFilterType = newSelectedFilterType,
                filterItems = updateFilterItemsSelection(state.filterItems, "liked", newSelectedFilterType == FilterType.LIKED)
            )
        }
        // In real app, filter venues based on selected filter
    }
    
    private fun handleFilterItemClicked(filterId: String) = intent {
        when (filterId) {
            "filter_button" -> {
                // Filter button click is handled by OnFilterClicked intent
                // This shouldn't be called, but handle it gracefully
            }
            "liked" -> {
                handleFilterTypeSelected(FilterType.LIKED)
            }
            else -> {
                // Dynamic filter clicked
                reduce {
                    state.copy(
                        filterItems = updateFilterItemsSelection(state.filterItems, filterId, null)
                    )
                }
                // In real app, filter venues based on selected dynamic filters
            }
        }
    }
    
    private fun updateFilterItemsSelection(
        filterItems: List<FilterItem>,
        filterId: String,
        isSelected: Boolean?
    ): List<FilterItem> {
        return filterItems.map { item ->
            if (item.id == filterId) {
                item.copy(isSelected = isSelected ?: !item.isSelected)
            } else {
                item
            }
        }
    }
    
    private fun handleMapMarkerClicked(venueId: String) = intent {
        // Find merchant offers for this venue
        val merchantOffers = getMockMerchantOffers()
        val selectedMerchant = merchantOffers.find { it.merchantId == venueId }
        val slots = selectedMerchant?.slots ?: emptyList()
        val merchantName = selectedMerchant?.merchantName ?: ""
        
        reduce {
            // Select clicked marker and deselect all others
            val updatedMarkers = state.markers.map { marker ->
                val markerVenueId = marker.tag as? String ?: marker.id
                if (markerVenueId == venueId) {
                    // Always select the clicked marker
                    marker.copy(isSelected = true)
                } else {
                    // Deselect all other markers
                    marker.copy(isSelected = false)
                }
            }
            state.copy(
                markers = updatedMarkers,
                selectedVenueId = venueId,
                selectedMerchantName = merchantName,
                selectedMerchantSlots = slots
            )
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
            // Deselect all markers when map is clicked
            val updatedMarkers = state.markers.map { marker ->
                marker.copy(isSelected = false)
            }
            state.copy(
                markers = updatedMarkers,
                selectedVenueId = null,
                selectedMerchantName = "",
                selectedMerchantSlots = emptyList()
            )
        }
    }
    
    private fun handleVenueItemClicked(venueId: String) = intent {
        postSideEffect(ExploreSideEffect.NavigateToVenueDetail(venueId))
    }
    
    private fun handleFilterSheetDismissed() = intent {
        reduce {
            state.copy(isFilterSheetVisible = false)
        }
    }
    
    private fun handleFilterOptionClicked(categoryId: String, optionId: String) = intent {
        // Toggle filter option selection
        val updatedFilterData = state.filterData.copy(
            categories = state.filterData.categories.map { category ->
                if (category.id == categoryId) {
                    category.copy(
                        options = category.options.map { option ->
                            if (option.id == optionId) {
                                option.copy(isSelected = !option.isSelected)
                            } else {
                                option
                            }
                        }
                    )
                } else {
                    category
                }
            }
        )
        
        reduce {
            state.copy(filterData = updatedFilterData)
        }
    }
    
    private fun handleApplyFilters() = intent {
        // Apply selected filters and close the sheet
        // In a real app, this would trigger a filter API call
        // For now, just close the sheet
        reduce {
            state.copy(isFilterSheetVisible = false)
        }
        
        // TODO: Apply filters to markers/venues
        // Example: filter markers based on selected filter options
    }
    
    private fun loadFilterData() = intent {
        reduce {
            state.copy(filterData = getMockFilterData())
        }
    }
    
    // Mock data - replace with repository calls in real app
    private fun getMockDynamicFilters(): List<Pair<String, String>> {
        return listOf(
            "open_now" to "Open now",
            "nearest" to "Nearest",
            "hot_deal" to "Hot deal"
        )
    }
    
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
    
    /**
     * Mock merchant offers from backend
     * In real app, this would be fetched from API
     */
    private fun getMockMerchantOffers(): List<MerchantOffer> {
        return listOf(
            MerchantOffer(
                merchantId = "1",
                merchantName = "Donut Shop",
                merchantLogoUrl = "test_merchant_logo",
                coordinates = LatLong(40.4093, 49.8671), // Center of Baku
                slots = listOf(
                    OfferSlot(
                        id = "slot_1_1",
                        title = "Mixed donut bag",
                        price = "12.99",
                        pickupTime = "17:00 - 18:00",
                        imageUrl = null
                    ),
                    OfferSlot(
                        id = "slot_1_2",
                        title = "Chocolate donut pack",
                        price = "10.50",
                        pickupTime = "18:00 - 19:00",
                        imageUrl = null
                    )
                )
            ),
            MerchantOffer(
                merchantId = "2",
                merchantName = "Sandwich Place",
                merchantLogoUrl = "test_merchant_logo",
                coordinates = LatLong(40.3950, 49.8822), // Near Caspian Sea
                slots = listOf(
                    OfferSlot(
                        id = "slot_2_1",
                        title = "Fresh sandwich pack",
                        price = "15.50",
                        pickupTime = "12:00 - 13:00",
                        imageUrl = null
                    )
                )
            ),
            MerchantOffer(
                merchantId = "3",
                merchantName = "Coffee House",
                merchantLogoUrl = "test_merchant_logo",
                coordinates = LatLong(40.4236, 49.8520), // Nizami Street area
                slots = listOf(
                    OfferSlot(
                        id = "slot_3_1",
                        title = "Coffee & pastries",
                        price = "8.99",
                        pickupTime = "08:00 - 09:00",
                        imageUrl = null
                    ),
                    OfferSlot(
                        id = "slot_3_2",
                        title = "Espresso bundle",
                        price = "7.50",
                        pickupTime = "09:00 - 10:00",
                        imageUrl = null
                    ),
                    OfferSlot(
                        id = "slot_3_3",
                        title = "Breakfast combo",
                        price = "11.00",
                        pickupTime = "10:00 - 11:00",
                        imageUrl = null
                    )
                )
            ),
            MerchantOffer(
                merchantId = "4",
                merchantName = "Bakery",
                merchantLogoUrl = "test_merchant_logo",
                coordinates = LatLong(40.3820, 49.8500), // Old City area
                slots = listOf(
                    OfferSlot(
                        id = "slot_4_1",
                        title = "Bakery special box",
                        price = "18.00",
                        pickupTime = "10:00 - 11:00",
                        imageUrl = null
                    ),
                    OfferSlot(
                        id = "slot_4_2",
                        title = "Fresh bread pack",
                        price = "5.50",
                        pickupTime = "11:00 - 12:00",
                        imageUrl = null
                    )
                )
            ),
            MerchantOffer(
                merchantId = "5",
                merchantName = "Grocery Store",
                merchantLogoUrl = "test_merchant_logo",
                coordinates = LatLong(40.4350, 49.8800), // Northern Baku
                slots = listOf(
                    OfferSlot(
                        id = "slot_5_1",
                        title = "Grocery surprise bag",
                        price = "22.50",
                        pickupTime = "09:00 - 10:00",
                        imageUrl = null
                    )
                )
            )
        )
    }
    
    /**
     * Mock filter data for filter bottom sheet
     * In real app, this would be fetched from backend
     */
    private fun getMockFilterData(): FilterData {
        return FilterData(
            categories = listOf(
                FilterCategory(
                    id = "cuisine",
                    title = "Cuisine",
                    options = listOf(
                        FilterOption(id = "italian", text = "Italian", isSelected = false),
                        FilterOption(id = "asian", text = "Asian", isSelected = false),
                        FilterOption(id = "mexican", text = "Mexican", isSelected = false),
                        FilterOption(id = "american", text = "American", isSelected = false),
                        FilterOption(id = "mediterranean", text = "Mediterranean", isSelected = false)
                    )
                ),
                FilterCategory(
                    id = "dietary",
                    title = "Dietary",
                    options = listOf(
                        FilterOption(id = "vegetarian", text = "Vegetarian", isSelected = false),
                        FilterOption(id = "vegan", text = "Vegan", isSelected = false),
                        FilterOption(id = "gluten_free", text = "Gluten Free", isSelected = false),
                        FilterOption(id = "halal", text = "Halal", isSelected = false),
                        FilterOption(id = "kosher", text = "Kosher", isSelected = false)
                    )
                ),
                FilterCategory(
                    id = "price_range",
                    title = "Price Range",
                    options = listOf(
                        FilterOption(id = "budget", text = "$", isSelected = false),
                        FilterOption(id = "moderate", text = "$$", isSelected = false),
                        FilterOption(id = "expensive", text = "$$$", isSelected = false)
                    )
                ),
                FilterCategory(
                    id = "rating",
                    title = "Rating",
                    options = listOf(
                        FilterOption(id = "4_plus", text = "4+ Stars", isSelected = false),
                        FilterOption(id = "4_5_plus", text = "4.5+ Stars", isSelected = false),
                        FilterOption(id = "5_stars", text = "5 Stars", isSelected = false)
                    )
                ),
                FilterCategory(
                    id = "distance",
                    title = "Distance",
                    options = listOf(
                        FilterOption(id = "under_1km", text = "Under 1 km", isSelected = false),
                        FilterOption(id = "under_2km", text = "Under 2 km", isSelected = false),
                        FilterOption(id = "under_5km", text = "Under 5 km", isSelected = false)
                    )
                )
            )
        )
    }
}

