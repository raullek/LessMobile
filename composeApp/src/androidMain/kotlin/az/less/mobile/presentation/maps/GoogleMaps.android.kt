package az.less.mobile.presentation.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.less.mobile.presentation.maps.models.CameraLocationBounds
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.LatLongZoom
import az.less.mobile.presentation.maps.models.Location
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
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
        position = shouldSetInitialCameraPosition?.let {
            GoogleCameraPosition.fromLatLngZoom(
                LatLng(it.target.latitude, it.target.longitude),
                it.zoom
            )
        } ?: GoogleCameraPosition.fromLatLngZoom(
            LatLng(40.4093, 49.8671), // Default to Baku
            12f
        )
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
        myLocationButtonEnabled = isTrackingEnabled,
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
            // Draw markers
            markers?.forEach { marker ->
                if (marker.isVisible) {
                    val markerState = rememberMarkerState(
                        position = LatLng(marker.position.latitude, marker.position.longitude)
                    )
                    
                    GoogleMarker(
                        state = markerState,
                        title = marker.title,
                        snippet = marker.snippet,
                        alpha = marker.alpha,
                        rotation = marker.rotation,
                        draggable = marker.isDraggable,
                        onClick = {
                            onMarkerInfoClick?.invoke(marker)
                            true
                        }
                    )
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