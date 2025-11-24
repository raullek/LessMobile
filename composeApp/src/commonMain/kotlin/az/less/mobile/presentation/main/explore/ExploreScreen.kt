package az.less.mobile.presentation.main.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.main.explore.components.ExploreVenueCard
import az.less.mobile.presentation.main.explore.components.FilterChip
import az.less.mobile.presentation.main.explore.models.FilterType
import az.less.mobile.presentation.main.offers.components.SearchFilterBar
import az.less.mobile.presentation.maps.GoogleMaps
import az.less.mobile.presentation.maps.LocationPermissionHandler
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.MapType
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful ExploreScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    
    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ExploreSideEffect.NavigateToVenueDetail -> {
                // Handle navigation to venue detail
                // navController.navigate("venue_detail/${sideEffect.venueId}")
            }
            is ExploreSideEffect.NavigateToSearch -> {
                // Handle navigation to search screen
                // navController.navigate("search")
            }
            is ExploreSideEffect.NavigateToFilter -> {
                // Handle navigation to filter screen
                // navController.navigate("filter")
            }
            is ExploreSideEffect.ShowError -> {
                // Show error snackbar
            }
        }
    }
    
    // Render the stateless UI
    ExploreScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless ExploreScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreenContent(
    state: ExploreState,
    onIntent: (ExploreIntent) -> Unit,

    modifier: Modifier = Modifier
) {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var locationPermissionDenied by remember { mutableStateOf(false) }

    // Material bottom sheet scaffold state
    val scaffoldState = rememberBottomSheetScaffoldState()

    // Update location permission in state
    LaunchedEffect(locationPermissionGranted) {
        onIntent(ExploreIntent.OnLocationPermissionChanged(locationPermissionGranted))
    }

    LocationPermissionHandler(
        onPermissionGranted = {
            locationPermissionGranted = true
            locationPermissionDenied = false
            println("✅ Location permission granted")
        },
        onPermissionDenied = {
            locationPermissionGranted = false
            locationPermissionDenied = true
            println("❌ Location permission denied")
        }
    ) {
        BottomSheetScaffold(
            modifier = modifier,
            scaffoldState = scaffoldState,
            sheetContainerColor = LessTheme.colors.backgroundSecond,
            sheetPeekHeight = 200.dp, // Make bottom sheet visible by default
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(bottom = LessTheme.size.large) // Push sheet content up from bottom nav bar
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = LessTheme.spacing.medium,
                            end = LessTheme.spacing.medium,
                            top = LessTheme.spacing.small,
                        )
                    ) {

                    // Venue Items
                    items(
                        items = state.venues,
                        key = { item -> item.id }
                    ) { item ->
                        ExploreVenueCard(
                            item = item,
                            onClick = {
                                onIntent(ExploreIntent.OnVenueItemClicked(item.id))
                            }
                        )

                        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
                    }
                }
                }
            },
        ) { paddingValues ->
            // Map is full screen, ignoring bottom sheet padding
            Box(modifier = Modifier.fillMaxSize()) {
                // Google Maps - full screen behind bottom sheet
                GoogleMaps(
                    modifier = Modifier.fillMaxSize(),
                    shouldSetInitialCameraPosition = CameraPosition(
                        target = LatLong(40.4093, 49.8671), // Center on Baku
                        zoom = 13f
                    ),
                    mapType = MapType.NORMAL,
                    isZoomControlsVisible = false,
                    isCompassVisible = true,
                    isTrackingEnabled = locationPermissionGranted,
                    markers = emptyList(), // No markers for now
                    onMarkerInfoClick = { marker ->
                        onIntent(ExploreIntent.OnMapMarkerClicked(marker.id))
                    },
                    onMapClick = { latLong ->
                        onIntent(ExploreIntent.OnMapClick(latLong))
                    },
                    onFindMeButtonClick = null // Remove recenter location button
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(
                            top = LessTheme.spacing.medium,
                            start = LessTheme.spacing.medium,
                            end = LessTheme.spacing.medium,)
                        .background(color = Color.Transparent)
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilterType.entries.forEach { filterType ->
                        FilterChip(
                            text = filterType.displayName,
                            isSelected = state.selectedFilterType == filterType,
                            showIconContainer = filterType == FilterType.LIKED, // Icon only for Liked
                            onClick = {
                                onIntent(ExploreIntent.OnFilterTypeSelected(filterType))
                            }
                        )
                    }
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(LessTheme.spacing.medium)
                    .background(LessTheme.colors.backgroundSecond)
                    .align(Alignment.BottomCenter))

            }
        }
    }
}
