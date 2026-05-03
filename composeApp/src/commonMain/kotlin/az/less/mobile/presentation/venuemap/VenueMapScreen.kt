package az.less.mobile.presentation.venuemap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import az.less.mobile.presentation.maps.GoogleMaps
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker
import az.less.mobile.presentation.partner.preview.VenuePreviewViewModel
import az.less.mobile.utils.openDirections
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_left_24dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

/**
 * Standalone fullscreen map screen showing a single venue pinned at its
 * coordinates, with a "Show Direction" button that hands off to the user's
 * preferred external navigation app (Google Maps / Waze / Apple Maps).
 *
 * Reuses [VenuePreviewViewModel] purely as a venue-data loader (lat/lng/name)
 * — no `isMapVisible` toggle, no profile body to swap to, single back-stack
 * entry. Tapping back returns to whichever screen launched it (orders bottom
 * sheet, merchant profile, etc.) without an intermediate hop.
 */
@Composable
fun VenueMapScreen(
    venueId: String,
    viewModel: VenuePreviewViewModel = koinViewModel(),
    navController: NavController
) {
    viewModel.initialize(venueId)
    val state by viewModel.collectAsState()

    val merchantName = state.merchantName
    val latitude = state.latitude
    val longitude = state.longitude

    Box(modifier = Modifier.fillMaxSize()) {
        if (!state.isLoading && (latitude != 0.0 || longitude != 0.0)) {
            val target = LatLong(latitude, longitude)
            GoogleMaps(
                modifier = Modifier.fillMaxSize(),
                shouldSetInitialCameraPosition = CameraPosition(
                    target = target,
                    zoom = 16f
                ),
                mapType = MapType.NORMAL,
                isZoomControlsVisible = false,
                isCompassVisible = true,
                isTrackingEnabled = false,
                markers = listOf(
                    Marker(
                        id = "venue_location",
                        position = target,
                        title = merchantName,
                        isVisible = true,
                        tag = "location_pin"
                    )
                ),
                onFindMeButtonClick = null
            )
        }

        // Back button
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = LessTheme.spacing.medium, top = LessTheme.spacing.medium)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0x80171A1C))
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_chevron_left_24dp),
                contentDescription = "Back",
                tint = LessTheme.colors.backgroundPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        // Show Direction button — hands off to the external maps/navigation app.
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
                    if (latitude != 0.0 || longitude != 0.0) {
                        openDirections(latitude, longitude, merchantName)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large,
                enabled = !state.isLoading && (latitude != 0.0 || longitude != 0.0)
            )
        }
    }
}
