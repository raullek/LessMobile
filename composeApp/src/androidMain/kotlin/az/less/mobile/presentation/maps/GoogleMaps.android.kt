package az.less.mobile.presentation.maps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.key
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil3.imageLoader
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.SuccessResult
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.test_merchant_logo
import org.jetbrains.compose.resources.painterResource
import az.less.mobile.presentation.maps.models.CameraLocationBounds
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.LatLongZoom
import az.less.mobile.presentation.maps.models.Location
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import lessmobile.composeapp.generated.resources.ill_venue_placeholder
import com.google.android.gms.maps.model.CameraPosition as GoogleCameraPosition
import com.google.maps.android.compose.Marker as GoogleMarker

/**
 * Android implementation of GoogleMaps using Google Maps SDK
 */
@Composable
actual fun GoogleMaps(
    modifier: Modifier,
    isMapOptionSwitchesVisible: Boolean,
    isZoomControlsVisible: Boolean,
    isCompassVisible: Boolean,
    mapType: MapType,
    isTrackingEnabled: Boolean,
    userLocation: LatLong?,
    onToggleIsTrackingEnabledClick: (() -> Unit)?,
    onFindMeButtonClick: (() -> Unit)?,
    markers: List<Marker>?,
    shouldCalcClusterItems: Boolean,
    onDidCalculateClusterItemList: () -> Unit,
    shouldShowInfoMarker: Marker?,
    onDidShowInfoMarker: () -> Unit,
    onMarkerInfoClick: ((Marker) -> Unit)?,
    shouldSetInitialCameraPosition: CameraPosition?,
    shouldCenterCameraOnLatLong: LatLong?,
    onDidCenterCameraOnLatLong: () -> Unit,
    cameraLocationBounds: CameraLocationBounds?,
    shouldZoomToLatLongZoom: LatLongZoom?,
    onDidZoomToLatLongZoom: () -> Unit,
    onMapClick: ((LatLong) -> Unit)?,
    onMapLongClick: ((LatLong) -> Unit)?,
    polyLine: List<LatLong>?,
    polyLineColor: Long,
    polyLineWidth: Float,
    seenRadiusMiles: Double,
    cachedMarkersLastUpdatedLocation: Location?,
    isMarkersLastUpdatedLocationVisible: Boolean,
    shouldAllowCacheReset: Boolean,
    onDidAllowCacheReset: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = when {
            isTrackingEnabled && userLocation != null -> GoogleCameraPosition.fromLatLngZoom(
                LatLng(userLocation.latitude, userLocation.longitude),
                15f
            )
            shouldSetInitialCameraPosition != null -> GoogleCameraPosition.fromLatLngZoom(
                LatLng(shouldSetInitialCameraPosition.target.latitude, shouldSetInitialCameraPosition.target.longitude),
                shouldSetInitialCameraPosition.zoom
            )
            else -> GoogleCameraPosition.fromLatLngZoom(
                LatLng(40.4093, 49.8671), // Default to Baku
                12f
            )
        }
    }

    // Center on user location when tracking becomes enabled
    LaunchedEffect(isTrackingEnabled, userLocation) {
        if (isTrackingEnabled && userLocation != null) {
            cameraPositionState.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                    LatLng(userLocation.latitude, userLocation.longitude),
                    15f
                )
            )
        }
    }

    // Handle camera position changes
    LaunchedEffect(shouldCenterCameraOnLatLong) {
        shouldCenterCameraOnLatLong?.let {
            cameraPositionState.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLng(
                    LatLng(it.latitude, it.longitude)
                )
            )
            onDidCenterCameraOnLatLong()
        }
    }

    // Handle zoom changes
    LaunchedEffect(shouldZoomToLatLongZoom) {
        shouldZoomToLatLongZoom?.let {
            cameraPositionState.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latLong.latitude, it.latLong.longitude),
                    it.zoom
                )
            )
            onDidZoomToLatLongZoom()
        }
    }

    // Handle bounds changes
    LaunchedEffect(cameraLocationBounds) {
        cameraLocationBounds?.let {
            val bounds = LatLngBounds(
                LatLng(it.southwest.latitude, it.southwest.longitude),
                LatLng(it.northeast.latitude, it.northeast.longitude)
            )
            cameraPositionState.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    it.padding
                )
            )
        }
    }

    val mapProperties = MapProperties(
        isMyLocationEnabled = isTrackingEnabled,
        mapType = when (mapType) {
            MapType.NORMAL -> com.google.maps.android.compose.MapType.NORMAL
            MapType.SATELLITE -> com.google.maps.android.compose.MapType.SATELLITE
            MapType.TERRAIN -> com.google.maps.android.compose.MapType.TERRAIN
            MapType.HYBRID -> com.google.maps.android.compose.MapType.HYBRID
        }
    )

    val uiSettings = MapUiSettings(
        zoomControlsEnabled = isZoomControlsVisible,
        compassEnabled = isCompassVisible,
        myLocationButtonEnabled = false,
        mapToolbarEnabled = isMapOptionSwitchesVisible
    )

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapClick = { latLng ->
                onMapClick?.invoke(LatLong(latLng.latitude, latLng.longitude))
            },
            onMapLongClick = { latLng ->
                onMapLongClick?.invoke(LatLong(latLng.latitude, latLng.longitude))
            }
        ) {
            // Draw markers using MarkerComposable for custom Compose UI
            // Based on: https://stackoverflow.com/questions/79020838/how-to-create-custom-google-maps-marker-icon-in-compose
            markers?.forEach { marker ->
                if (marker.isVisible) {
                    // Use key to ensure proper recomposition when marker properties change
                    key("${marker.id}_${marker.isSelected}_${marker.position}") {
                        val markerState = rememberMarkerState(
                            key = marker.id,
                            position = LatLng(marker.position.latitude, marker.position.longitude)
                        )

                        // Update marker position when it changes
                        LaunchedEffect(marker.position) {
                            markerState.position = LatLng(marker.position.latitude, marker.position.longitude)
                        }

                        // Pre-load image outside MarkerComposable so the bitmap is ready
                        val context = LocalContext.current
                        var loadedBitmap by remember(marker.iconUrl) { mutableStateOf<ImageBitmap?>(null) }

                        LaunchedEffect(marker.iconUrl) {
                            val url = marker.iconUrl
                            if (url != null) {
                                val request = ImageRequest.Builder(context)
                                    .data(url)
                                    .allowHardware(false)
                                    .build()
                                val result = context.imageLoader.execute(request)
                                if (result is SuccessResult) {
                                    loadedBitmap = result.image.asDrawable(context.resources).toBitmap().asImageBitmap()
                                }
                            }
                        }

                        MarkerComposable(
                            keys = arrayOf<Any>(marker.id, marker.isSelected, loadedBitmap ?: "null"),
                            state = markerState,
                            onClick = {
                                if (marker.itemsCount > 0) {
                                    onMarkerInfoClick?.invoke(marker)
                                }
                                true
                            }
                        ) {
                            // Check if it's a location pin marker
                            if (marker.tag == "location_pin") {
                                LocationPinMarker()
                            } else {
                                val isSelected = marker.isSelected
                                val slotCount = marker.itemsCount
                                CustomMerchantMarker(
                                    logoBitmap = loadedBitmap,
                                    title = marker.title,
                                    snippet = marker.snippet,
                                    isSelected = isSelected,
                                    slotCount = slotCount
                                )
                            }
                        }
                    }
                }
            }

            // Draw polyline
            polyLine?.let { points ->
                if (points.isNotEmpty()) {
                    Polyline(
                        points = points.map { LatLng(it.latitude, it.longitude) },
                        color = androidx.compose.ui.graphics.Color(polyLineColor),
                        width = polyLineWidth
                    )
                }
            }
        }
    }
}

/**
 * Custom merchant marker composable for Google Maps
 * Uses MarkerComposable to display custom Compose UI as marker
 * Based on: https://stackoverflow.com/questions/79020838/how-to-create-custom-google-maps-marker-icon-in-compose
 * 
 * @param iconUrl URL of the merchant logo to download
 * @param title Optional title text to display
 * @param snippet Optional snippet text to display
 * @param isSelected Whether this marker is currently selected
 * @param slotCount Number of available slots (shown as badge in top-right)
 */
@Composable
private fun CustomMerchantMarker(
    logoBitmap: ImageBitmap? = null,
    title: String? = null,
    snippet: String? = null,
    isSelected: Boolean = false,
    slotCount: Int = 0
) {
    val markerSize = if (isSelected) 40.dp else 36.dp
    val shape = RoundedCornerShape(12.dp)

    Box {
        val painter = if (logoBitmap != null) {
            BitmapPainter(logoBitmap)
        } else {
            painterResource(Res.drawable.ill_venue_placeholder)
        }
        Image(
            painter = painter,
            contentDescription = title ?: "Merchant marker",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 8.dp, end = 8.dp)
                .size(markerSize)
                .clip(shape)
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            color = LessTheme.colors.textIconsBrand,
                            shape = shape
                        )
                    } else {
                        Modifier
                    }
                )
        )

        // Slot count badge in top-right corner (based on Figma design)
        if (slotCount > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = LessTheme.colors.textIconsBrand,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = slotCount.toString(),
                    style = LessTheme.typography.caption12Regular,
                    color = LessTheme.colors.textIconsNested,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Location pin marker for selecting a location on the map
 * Uses ic_explore_24dp icon with primary brand tint
 */
@Composable
private fun LocationPinMarker() {
        Icon(
            painter = painterResource(Res.drawable.ic_explore_24dp),
            contentDescription = "Selected location",
            tint = LessTheme.colors.elementsPrimaryBrand,
            modifier = Modifier.size(24.dp)
        )
    }