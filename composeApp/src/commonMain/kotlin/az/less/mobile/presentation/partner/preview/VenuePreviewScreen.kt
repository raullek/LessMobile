package az.less.mobile.presentation.partner.preview

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.client.main.merchant.MerchantProfileTab
import az.less.mobile.presentation.client.main.merchant.components.CollapsingMerchantToolbar
import az.less.mobile.presentation.client.main.merchant.components.MerchantOfferCard
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileContactSection
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileInfoSection
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileShimmer
import az.less.mobile.presentation.client.main.merchant.components.MerchantProfileTabsSection
import az.less.mobile.presentation.client.main.merchant.components.MerchantReviewCard
import az.less.mobile.presentation.client.main.merchant.components.RatingSummaryCard
import az.less.mobile.presentation.maps.GoogleMaps
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker
import az.less.mobile.utils.openDirections
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_left_24dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Read-only venue preview screen used by both merchant and partner flows
 * to show what their venue looks like to clients. Mirrors
 * [az.less.mobile.presentation.client.main.merchant.MerchantProfileScreen]
 * but disables every interaction that would otherwise mutate state or
 * navigate to a reserve/box flow.
 */
@Composable
fun VenuePreviewScreen(
    venueId: String,
    viewModel: VenuePreviewViewModel = koinViewModel(),
    navController: NavController
) {
    viewModel.initialize(venueId)
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is VenuePreviewSideEffect.NavigateBack -> {
                navController.popBackStack()
            }
            is VenuePreviewSideEffect.ShowError -> {
                // Show error snackbar
            }
        }
    }

    VenuePreviewScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenuePreviewScreenContent(
    state: VenuePreviewState,
    onIntent: (VenuePreviewIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        MerchantProfileShimmer(modifier = modifier)
    } else if (state.isMapVisible) {
        VenuePreviewMapView(
            merchantName = state.merchantName,
            latitude = state.latitude,
            longitude = state.longitude,
            address = state.address,
            onBackClick = { onIntent(VenuePreviewIntent.OnMapBackClicked) },
            modifier = modifier
        )
    } else {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            state = rememberTopAppBarState()
        )
        val lazyListState = rememberLazyListState()
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CollapsingMerchantToolbar(
                    title = state.merchantName,
                    heroImageUrl = state.heroImageUrl,
                    merchantLogoUrl = state.merchantLogoUrl,
                    onBackClick = { onIntent(VenuePreviewIntent.OnBackClicked) },
                    scrollBehavior = scrollBehavior,
                    lazyListState = lazyListState
                )
            },
            containerColor = LessTheme.colors.backgroundSecond
        ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LessTheme.colors.backgroundSecond)
        ) {
            item(key = "merchant_info") {
                MerchantProfileInfoSection(
                    merchantName = state.merchantName,
                    description = state.merchantDescription,
                    rating = state.rating,
                    distance = ""
                )
            }

            item(key = "contact_section") {
                MerchantProfileContactSection(
                    phoneNumber = state.phoneNumber,
                    address = state.address,
                    onPhoneClick = {},
                    onViewLocationClick = { onIntent(VenuePreviewIntent.OnViewLocationClicked) },
                    onDirectionsClick = {}
                )
            }

            item(key = "tabs_section") {
                MerchantProfileTabsSection(
                    selectedTab = state.selectedTab,
                    onTabSelected = { onIntent(VenuePreviewIntent.OnTabSelected(it)) }
                )
            }

            when (state.selectedTab) {
                MerchantProfileTab.OFFERS -> {
                    if (state.offers.isEmpty()) {
                        item(key = "offers_empty") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No offers yet",
                                    style = LessTheme.typography.body16Regular,
                                    color = LessTheme.colors.textIconsGrey
                                )
                            }
                        }
                    } else {
                        items(
                            items = state.offers,
                            key = { it.id }
                        ) { offer ->
                            MerchantOfferCard(
                                offer = offer,
                                merchantName = state.merchantName,
                                merchantLogoUrl = state.merchantLogoUrl,
                                rating = state.rating,
                                distance = "",
                                onClick = null,
                                modifier = Modifier.padding(
                                    horizontal = LessTheme.spacing.medium,
                                    vertical = LessTheme.spacing.xSmall
                                )
                            )
                        }
                    }
                }
                MerchantProfileTab.REVIEWS -> {
                    if (state.reviews.isEmpty() && state.ratingCount == 0) {
                        item(key = "reviews_empty") {
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
                    } else {
                        item(key = "rating_summary") {
                            RatingSummaryCard(
                                averageRating = state.rating,
                                totalReviews = state.reviewsTotal,
                                distribution = state.ratingDistribution,
                                modifier = Modifier.padding(
                                    horizontal = LessTheme.spacing.medium,
                                    vertical = LessTheme.spacing.xSmall
                                )
                            )
                        }

                        items(
                            items = state.reviews,
                            key = { it.id }
                        ) { review ->
                            MerchantReviewCard(
                                review = review,
                                modifier = Modifier.padding(
                                    horizontal = LessTheme.spacing.medium,
                                    vertical = LessTheme.spacing.xSmall
                                )
                            )
                        }
                    }
                }
            }

            item(key = "bottom_spacing") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
            }
        }
        }
    }
}

@Composable
private fun VenuePreviewMapView(
    merchantName: String,
    latitude: Double,
    longitude: Double,
    address: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val merchantLocation = LatLong(latitude, longitude)
    val marker = Marker(
        id = "venue_location",
        position = merchantLocation,
        title = merchantName,
        isVisible = true,
        tag = "location_pin"
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
                text = "Show Direction",
                onClick = {
                    openDirections(latitude, longitude, merchantName)
                },
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large
            )
        }
    }
}
