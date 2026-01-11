package az.less.mobile.presentation.client.main.merchant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.navigation.ClientRoute
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileContactSection
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileHeroSection
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileInfoSection
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileOfferCard
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileTabsSection
import az.less.mobile.presentation.client.reserve.ReserveScreen
import az.less.mobile.presentation.maps.GoogleMaps
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker
import az.less.mobile.utils.shareContent
import kotlinx.coroutines.launch
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_left_24dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful MerchantProfileScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantProfileScreen(
    viewModel: MerchantProfileViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    val scope = rememberCoroutineScope()

    // Reserve bottom sheet state
    var isReserveBottomSheetVisible by remember { mutableStateOf(false) }
    val reserveSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MerchantProfileSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is MerchantProfileSideEffect.NavigateToReserve -> {
                isReserveBottomSheetVisible = true
                scope.launch {
                    reserveSheetState.expand()
                }
            }
            is MerchantProfileSideEffect.ShowError -> {
                // Show error snackbar
            }
            is MerchantProfileSideEffect.OpenDirections -> {
                // Open maps app with directions
            }
            is MerchantProfileSideEffect.CallPhone -> {
                // Open phone dialer
            }
        }
    }

    // Render the stateless UI
    MerchantProfileScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )

    // Reserve Bottom Sheet
    if (isReserveBottomSheetVisible) {
        ReserveScreen(
            isVisible = isReserveBottomSheetVisible,
            sheetState = reserveSheetState,
            viewModel = koinViewModel(),
            onDismiss = {
                scope.launch {
                    reserveSheetState.hide()
                }.invokeOnCompletion {
                    isReserveBottomSheetVisible = false
                }
            },
            onOrderPlaced = { orderInfo ->
                navController.navigate(
                    ClientRoute.OrderAccepted(
                        orderNumber = orderInfo.orderNumber,
                        venueName = orderInfo.venueName,
                        pickupTime = orderInfo.pickupTime
                    )
                )
            },
            onNavigateToMerchant = { merchantId ->
                // Already on merchant profile screen
            }
        )
    }
}

/**
 * Stateless MerchantProfileScreen UI implementation
 * Based on Figma design: Merchant Profile
 */
@Composable
fun MerchantProfileScreenContent(
    state: MerchantProfileState,
    onIntent: (MerchantProfileIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isMapVisible) {
        // Map View
        MerchantMapView(
            merchantName = state.merchantName,
            latitude = state.latitude,
            longitude = state.longitude,
            address = state.address,
            onBackClick = { onIntent(MerchantProfileIntent.OnMapBackClicked) },
            onShowDirectionsClick = { onIntent(MerchantProfileIntent.OnDirectionsClicked) },
            modifier = modifier
        )
    } else {
        // Profile Content
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(LessTheme.colors.backgroundSecond)
        ) {
            // Hero Section with overlapping logo
            item(key = "hero_section") {
                MerchantProfileHeroSection(
                    isFavorite = state.isFavorite,
                    onBackClick = { onIntent(MerchantProfileIntent.OnBackClicked) },
                    onFavoriteClick = { onIntent(MerchantProfileIntent.OnFavoriteClicked) }
                )
            }

            // Merchant Info Section
            item(key = "merchant_info") {
                MerchantProfileInfoSection(
                    merchantName = state.merchantName,
                    description = state.merchantDescription,
                    rating = state.rating,
                    distance = state.distance
                )
            }

            // Contact Section
            item(key = "contact_section") {
                MerchantProfileContactSection(
                    phoneNumber = state.phoneNumber,
                    address = state.address,
                    onPhoneClick = { onIntent(MerchantProfileIntent.OnPhoneClicked) },
                    onViewLocationClick = { onIntent(MerchantProfileIntent.OnViewLocationClicked) },
                    onDirectionsClick = { onIntent(MerchantProfileIntent.OnDirectionsClicked) }
                )
            }

            // Tabs Section
            item(key = "tabs_section") {
                MerchantProfileTabsSection(
                    selectedTab = state.selectedTab,
                    onTabSelected = { onIntent(MerchantProfileIntent.OnTabSelected(it)) }
                )
            }

            // Offers List
            if (state.selectedTab == MerchantProfileTab.OFFERS) {
                items(
                    items = state.offers,
                    key = { offer -> offer.id }
                ) { offer ->
                    MerchantProfileOfferCard(
                        offer = offer,
                        onClick = { onIntent(MerchantProfileIntent.OnOfferClicked(offer.id)) },
                        modifier = Modifier.padding(
                            horizontal = LessTheme.spacing.medium,
                            vertical = LessTheme.spacing.xSmall
                        )
                    )
                }

                // Bottom spacing
                item(key = "bottom_spacing") {
                    Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
                }
            } else {
                // Reviews tab - placeholder
                item(key = "reviews_placeholder") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No reviews yet",
                            style = LessTheme.typography.body16Regular,
                            color = LessTheme.colors.textIconsGrey
                        )
                    }
                }
            }
        }
    }
}

/**
 * Full-screen map view showing merchant location
 */
@Composable
private fun MerchantMapView(
    merchantName: String,
    latitude: Double,
    longitude: Double,
    address: String,
    onBackClick: () -> Unit,
    onShowDirectionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val merchantLocation = LatLong(latitude, longitude)
    val marker = Marker(
        id = "merchant_location",
        position = merchantLocation,
        title = merchantName,
        isVisible = true
    )

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMaps(
            modifier = Modifier.fillMaxSize(),
            shouldSetInitialCameraPosition = CameraPosition(
                target = merchantLocation,
                zoom = 16f
            ),
            mapType = MapType.NORMAL,
            isZoomControlsVisible = false,
            isCompassVisible = true,
            isTrackingEnabled = false,
            markers = listOf(marker),
            onFindMeButtonClick = null
        )

        // Back Button
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = LessTheme.spacing.medium, top = LessTheme.spacing.medium)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0x80171A1C))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_left_24dp),
                contentDescription = "Back",
                tint = LessTheme.colors.backgroundPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Show Direction Button at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.medium
                )
        ) {
            DsButton(
                text = "ShowDirection",
                onClick = {
                    // Share location with geo URI for map applications
                    val shareText = buildString {
                        append("📍 $merchantName\n")
                        append("$address\n")
                        append("geo:$latitude,$longitude")
                    }
                    shareContent(shareText)
                    onShowDirectionsClick()
                },
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large
            )
        }
    }
}
