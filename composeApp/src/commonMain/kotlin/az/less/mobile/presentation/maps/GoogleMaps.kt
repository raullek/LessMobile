package az.less.mobile.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import az.less.mobile.presentation.maps.models.CameraLocationBounds
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.LatLongZoom
import az.less.mobile.presentation.maps.models.Location
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker

/**
 * Cross-platform Google Maps composable
 * 
 * This is an expect declaration that must be implemented for each platform:
 * - Android: Uses Google Maps SDK for Android
 * - iOS: Uses MapKit (Apple Maps)
 */
@Composable
expect fun GoogleMaps(
    modifier: Modifier = Modifier,
    // Map UI Controls
    isMapOptionSwitchesVisible: Boolean = true,
    isZoomControlsVisible: Boolean = true,
    isCompassVisible: Boolean = true,
    mapType: MapType = MapType.NORMAL,
    isDarkTheme: Boolean = false,
    
    // Location Tracking
    isTrackingEnabled: Boolean = false,
    userLocation: LatLong? = null,
    onToggleIsTrackingEnabledClick: (() -> Unit)? = null,
    onFindMeButtonClick: (() -> Unit)? = null,
    
    // Markers
    markers: List<Marker>? = null,
    shouldCalcClusterItems: Boolean = false,
    onDidCalculateClusterItemList: () -> Unit = {},
    shouldShowInfoMarker: Marker? = null,
    onDidShowInfoMarker: () -> Unit = {},
    onMarkerInfoClick: ((Marker) -> Unit)? = null,
    
    // Camera Control
    shouldSetInitialCameraPosition: CameraPosition? = null,
    shouldCenterCameraOnLatLong: LatLong? = null,
    onDidCenterCameraOnLatLong: () -> Unit = {},
    cameraLocationBounds: CameraLocationBounds? = null,
    shouldZoomToLatLongZoom: LatLongZoom? = null,
    onDidZoomToLatLongZoom: () -> Unit = {},
    
    // Map Interactions
    onMapClick: ((LatLong) -> Unit)? = null,
    onMapLongClick: ((LatLong) -> Unit)? = null,
    
    // Polyline/Path
    polyLine: List<LatLong>? = null,
    polyLineColor: Long = 0xFF0000FF, // Blue
    polyLineWidth: Float = 5f,
    
    // Additional Features
    seenRadiusMiles: Double = 0.5,
    cachedMarkersLastUpdatedLocation: Location? = null,
    isMarkersLastUpdatedLocationVisible: Boolean = false,
    shouldAllowCacheReset: Boolean = false,
    onDidAllowCacheReset: () -> Unit = {},
)






