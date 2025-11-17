package az.less.mobile.presentation.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import az.less.mobile.presentation.maps.models.CameraLocationBounds
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.LatLongZoom
import az.less.mobile.presentation.maps.models.Location
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKCoordinateSpanMake
import platform.MapKit.MKMapType
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation

/**
 * iOS implementation of GoogleMaps using MapKit (Apple Maps)
 */
@OptIn(ExperimentalForeignApi::class)
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
    Box(modifier = modifier) {
        UIKitView(
            factory = {
                val mapView = MKMapView()
                
                // Configure map type
                mapView.mapType = when (mapType) {
                    MapType.NORMAL -> MKMapType.MKMapTypeStandard
                    MapType.SATELLITE -> MKMapType.MKMapTypeSatellite
                    MapType.TERRAIN -> MKMapType.MKMapTypeStandard
                    MapType.HYBRID -> MKMapType.MKMapTypeHybrid
                }
                
                // Configure UI settings
                mapView.setShowsCompass(isCompassVisible)
                mapView.setShowsUserLocation(isTrackingEnabled)
                
                // Set initial camera position
                shouldSetInitialCameraPosition?.let { position ->
                    val coordinate = CLLocationCoordinate2DMake(
                        position.target.latitude,
                        position.target.longitude
                    )
                    val region = MKCoordinateRegionMakeWithDistance(
                        coordinate,
                        (position.zoom * 1000).toDouble(), // Convert zoom to meters
                        (position.zoom * 1000).toDouble()
                    )
                    mapView.setRegion(region, animated = false)
                } ?: run {
                    // Default to Baku
                    val coordinate = CLLocationCoordinate2DMake(40.4093, 49.8671)
                    val region = MKCoordinateRegionMakeWithDistance(
                        coordinate,
                        12000.0,
                        12000.0
                    )
                    mapView.setRegion(region, animated = false)
                }
                
                mapView
            },
            modifier = Modifier.fillMaxSize(),
            update = { mapView ->
                // Update markers
                mapView.removeAnnotations(mapView.annotations)
                markers?.forEach { marker ->
                    if (marker.isVisible) {
                        val annotation = MKPointAnnotation()
                        annotation.setCoordinate(
                            CLLocationCoordinate2DMake(
                                marker.position.latitude,
                                marker.position.longitude
                            )
                        )
                        annotation.setTitle(marker.title)
                        annotation.setSubtitle(marker.snippet)
                        mapView.addAnnotation(annotation)
                    }
                }
                
                // Handle camera center
                shouldCenterCameraOnLatLong?.let {
                    val coordinate = CLLocationCoordinate2DMake(it.latitude, it.longitude)
                    mapView.setCenterCoordinate(coordinate, animated = true)
                    onDidCenterCameraOnLatLong()
                }
                
                // Handle zoom
                shouldZoomToLatLongZoom?.let {
                    val coordinate = CLLocationCoordinate2DMake(
                        it.latLong.latitude,
                        it.latLong.longitude
                    )
                    val region = MKCoordinateRegionMakeWithDistance(
                        coordinate,
                        (it.zoom * 1000).toDouble(),
                        (it.zoom * 1000).toDouble()
                    )
                    mapView.setRegion(region, animated = true)
                    onDidZoomToLatLongZoom()
                }
                
                // Handle bounds
                cameraLocationBounds?.let { bounds ->
                    val coordinate = CLLocationCoordinate2DMake(
                        (bounds.southwest.latitude + bounds.northeast.latitude) / 2,
                        (bounds.southwest.longitude + bounds.northeast.longitude) / 2
                    )
                    val latDelta = bounds.northeast.latitude - bounds.southwest.latitude
                    val lonDelta = bounds.northeast.longitude - bounds.southwest.longitude
                    val span = MKCoordinateSpanMake(latDelta * 1.2, lonDelta * 1.2)
                    val region = MKCoordinateRegionMake(coordinate, span)
                    mapView.setRegion(region, animated = true)
                }
                
                // Update tracking mode
                if (isTrackingEnabled) {
                    mapView.setShowsUserLocation(true)
                }
            }
        )
    }
}