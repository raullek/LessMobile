package az.less.mobile.presentation.merchant.places.edit.selectlocation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.navigation.MerchantRoute
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsToolBar
import az.less.mobile.presentation.maps.GoogleMaps
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_map_24dp
import lessmobile.composeapp.generated.resources.select_location_confirm
import lessmobile.composeapp.generated.resources.select_location_hint
import lessmobile.composeapp.generated.resources.select_location_selected
import lessmobile.composeapp.generated.resources.select_location_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful SelectBranchLocationOnMapScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun SelectBranchLocationOnMapScreen(
    initialLatitude: Double? = null,
    initialLongitude: Double? = null,
    initialAddress: String? = null,
    viewModel: SelectBranchLocationOnMapViewModel = koinViewModel(),
    navController: NavController,
) {
    val state by viewModel.collectAsState()

    // Initialize with coordinates and address - key on params to reinitialize when they change
    LaunchedEffect(initialLatitude, initialLongitude, initialAddress) {
        viewModel.initialize(initialLatitude, initialLongitude, initialAddress)
    }

    // Observe address input from InputAddressScreen via Navigation's SavedStateHandle
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(Unit) {
        savedStateHandle?.getStateFlow<String?>("input_address", null)?.collect { address ->
            if (address != null) {
                val latitude = savedStateHandle.get<Double>("input_latitude")
                val longitude = savedStateHandle.get<Double>("input_longitude")
                if (latitude != null && longitude != null) {
                    viewModel.onIntent(
                        SelectBranchLocationOnMapIntent.OnAddressInputResult(address, latitude, longitude)
                    )
                }
            }
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SelectBranchLocationOnMapSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is SelectBranchLocationOnMapSideEffect.LocationSelected -> {
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set("selected_latitude", sideEffect.location.latitude)
                    set("selected_longitude", sideEffect.location.longitude)
                    set("selected_address", sideEffect.address)
                }
                navController.popBackStack()
            }
            is SelectBranchLocationOnMapSideEffect.NavigateToInputAddress -> {
                navController.navigate(MerchantRoute.InputAddress)
            }
        }
    }

    SelectBranchLocationOnMapScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless SelectBranchLocationOnMapScreen UI implementation
 * Based on Figma design node 2356:10184
 */
@Composable
fun SelectBranchLocationOnMapScreenContent(
    state: SelectBranchLocationOnMapState,
    onIntent: (SelectBranchLocationOnMapIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Create marker for selected location
    val selectedLocationTitle = stringResource(Res.string.select_location_selected)
    val selectedLocationMarker = state.selectedLocation?.let { location ->
        listOf(
            Marker(
                id = "selected_location",
                position = location,
                title = selectedLocationTitle,
                tag = "location_pin", // Tag to identify as location pin marker
                isVisible = true
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Map takes full space (underneath the overlay)
        GoogleMaps(
            modifier = Modifier.fillMaxSize(),
            shouldSetInitialCameraPosition = state.initialLocation?.let {
                CameraPosition(
                    target = it,
                    zoom = 15f
                )
            },
            mapType = MapType.NORMAL,
            isMapOptionSwitchesVisible = false,
            isZoomControlsVisible = true,
            isCompassVisible = true,
            markers = selectedLocationMarker,
            onMapClick = { latLong ->
                onIntent(SelectBranchLocationOnMapIntent.OnMapClick(latLong))
            },
            onFindMeButtonClick = {
                onIntent(SelectBranchLocationOnMapIntent.OnMyLocationClick)
            }
        )

        // Top section with gradient overlay, header and address card
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0f)
                        )
                    )
                )
        ) {
            // Header/Toolbar
            DsToolBar(
                title = stringResource(Res.string.select_location_title),
                onBackClick = { onIntent(SelectBranchLocationOnMapIntent.OnBackClick) },
                backgroundColor = Color.Transparent
            )

            // Address card (clickable to open InputAddressScreen)
            AddressCard(
                address = state.selectedAddress.ifEmpty { stringResource(Res.string.select_location_hint) },
                onClick = { onIntent(SelectBranchLocationOnMapIntent.OnAddressCardClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LessTheme.spacing.medium)
                    .padding(bottom = LessTheme.spacing.medium)
            )
        }

        // Bottom confirm button (on map)
        DsButton(
            text = stringResource(Res.string.select_location_confirm),
            onClick = { onIntent(SelectBranchLocationOnMapIntent.OnConfirmClick) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(LessTheme.spacing.medium),
            variant = ButtonVariant.Primary,
            size = ButtonSize.Large,
            enabled = state.isConfirmEnabled
        )
    }
}

/**
 * Address card component matching Figma design
 */
@Composable
private fun AddressCard(
    address: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(LessTheme.colors.backgroundPrimary)
            .border(
                width = 1.dp,
                color = LessTheme.colors.borderPrimary,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_map_24dp),
            contentDescription = null,
            tint = LessTheme.colors.elementsPrimaryBrand,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = address,
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsBlack,
            modifier = Modifier.weight(1f)
        )
    }
}
