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
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.Foundation.NSCache
import platform.Foundation.NSData
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKAnnotationView
import platform.MapKit.MKCoordinateRegionMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKCoordinateSpanMake
import platform.MapKit.MKMapTypeStandard
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.MapKit.MKPointAnnotation
import platform.UIKit.UIColor
import platform.UIKit.UIGestureRecognizerStateEnded
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UILabel
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UIView
import platform.UIKit.systemBlueColor
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import kotlin.experimental.ExperimentalObjCName


/**
 * Custom annotation that extends MKPointAnnotation to hold Marker reference
 * This allows us to map annotation clicks back to the Marker object
 */
@OptIn(ExperimentalForeignApi::class)
private class MarkerAnnotation(
    val marker: Marker
) : MKPointAnnotation() {
    init {
        setCoordinate(
            CLLocationCoordinate2DMake(
                marker.position.latitude,
                marker.position.longitude
            )
        )
        setTitle(marker.title)
        setSubtitle(marker.snippet)
    }
}


/**
 * Simple image cache for downloaded marker icons
 */
@OptIn(ExperimentalForeignApi::class)
private object ImageCache {
    private val cache = NSCache()

    fun get(url: NSURL): UIImage? = cache.objectForKey(url) as? UIImage

    fun set(url: NSURL, image: UIImage) {
        cache.setObject(image, forKey = url)
    }
}

/**
 * Map view delegate to provide custom annotation views and handle marker selection
 */
@OptIn(ExperimentalForeignApi::class)
private class MapViewDelegate(
    private val onMarkerInfoClick: ((Marker) -> Unit)?
) : NSObject(), MKMapViewDelegateProtocol {

    private fun downloadImageAsync(
        urlString: String,
        imageView: UIImageView
    ) {
        //  https://picsum.photos/200/300 for testing
        val url = NSURL.URLWithString(urlString) ?: return

        // Check cache first
        ImageCache.get(url)?.let { cachedImage ->
            imageView.setImage(cachedImage)
            return
        }

        // Show gray background while loading
        imageView.setBackgroundColor(UIColor.lightGrayColor)

        // Download in background
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)) {
            val data = NSData.dataWithContentsOfURL(url)
            val downloadedImage = data?.let { UIImage.imageWithData(it) }

            // Update UI on main thread
            dispatch_async(dispatch_get_main_queue()) {
                if (downloadedImage != null) {
                    ImageCache.set(url, downloadedImage)
                    imageView.setImage(downloadedImage)
                    imageView.setBackgroundColor(null)
                }
            }
        }
    }

    /**
     * Create a location pin marker view (simple icon marker)
     * Similar to Android's LocationPinMarker
     */
    private fun createLocationPinAnnotationView(
        annotation: MarkerAnnotation,
        reuseIdentifier: String
    ): MKAnnotationView {
        val annotationView = MKAnnotationView(
            annotation = annotation,
            reuseIdentifier = reuseIdentifier
        )
        annotationView.canShowCallout = false

        // Remove any existing subviews
        annotationView.subviews.forEach { subview ->
            (subview as? UIView)?.removeFromSuperview()
        }

        // Create a simple icon view (24dp = 24.0 points)
        val iconSize = 24.0
        val iconView = UIImageView()
        iconView.setFrame(platform.CoreGraphics.CGRectMake(0.0, 0.0, iconSize, iconSize))
        
        // Use SF Symbol for location icon (mappin.circle.fill or location.fill)
        // Fallback to a simple colored circle if system image is not available
        val systemImage = UIImage.systemImageNamed("mappin.circle.fill")
        if (systemImage != null) {
            iconView.setImage(systemImage)
            // Tint with brand color (green as approximation)
            iconView.setTintColor(UIColor.greenColor)
        } else {
            // Fallback: create a simple colored circle
            iconView.setBackgroundColor(UIColor.greenColor) // Brand color
            iconView.layer.setCornerRadius(iconSize / 2.0)
            iconView.layer.setMasksToBounds(true)
        }
        
        iconView.setContentMode(platform.UIKit.UIViewContentMode.UIViewContentModeScaleAspectFit)

        annotationView.setFrame(platform.CoreGraphics.CGRectMake(0.0, 0.0, iconSize, iconSize))
        annotationView.addSubview(iconView)
        annotationView.setCenterOffset(platform.CoreGraphics.CGPointMake(0.0, -iconSize / 2.0))

        return annotationView
    }

    private fun createCustomAnnotationView(
        annotation: MarkerAnnotation,
        reuseIdentifier: String
    ): MKAnnotationView {
        val marker = annotation.marker
        
        // Check if it's a location pin marker
        if (marker.tag == "location_pin") {
            return createLocationPinAnnotationView(annotation, reuseIdentifier)
        }
        
        val markerSize = if (marker.isSelected) 40.0 else 36.0

        val annotationView = MKAnnotationView(
            annotation = annotation,
            reuseIdentifier = reuseIdentifier
        )
        annotationView.canShowCallout = false

        // Remove any existing subviews
        annotationView.subviews.forEach { subview ->
            (subview as? UIView)?.removeFromSuperview()
        }

        // Create container view
        val containerView = UIView()
        containerView.setFrame(platform.CoreGraphics.CGRectMake(0.0, 0.0, markerSize, markerSize))

        // Create image view for merchant logo
        val imageView = UIImageView()
        imageView.setFrame(platform.CoreGraphics.CGRectMake(0.0, 0.0, markerSize, markerSize))
        imageView.setContentMode(platform.UIKit.UIViewContentMode.UIViewContentModeScaleAspectFill)
        imageView.setClipsToBounds(true)

        // Download icon from URL or show gray background
        val iconUrl = marker.iconUrl
        if (iconUrl != null) {
            downloadImageAsync(iconUrl, imageView)
        } else {
            // Gray background if no URL
            imageView.setBackgroundColor(UIColor.lightGrayColor)
        }

        // Apply rounded corners (12dp = 12.0 points)
        imageView.layer.setCornerRadius(12.0)
        imageView.layer.setMasksToBounds(true)

        // Add border if selected (2dp = 2.0 points)
        if (marker.isSelected) {
            imageView.layer.setBorderWidth(2.0)
            // Brand color - using systemBlue as approximation
            imageView.layer.setBorderColor(UIColor.greenColor.CGColor())
        } else {
            imageView.layer.setBorderWidth(0.0)
        }

        containerView.addSubview(imageView)

        // Add badge for slot count if > 1
        if (marker.itemsCount > 1) {
            val badgeSize = 16.0
            val badgeView = UIView()
            badgeView.setFrame(
                platform.CoreGraphics.CGRectMake(
                    markerSize - badgeSize - 4.0,
                    -4.0,
                    badgeSize,
                    badgeSize
                )
            )
            //  badgeView.setBackgroundColor(UIColor.systemBlue) // Brand color
            badgeView.layer.setCornerRadius(8.0)
            badgeView.layer.setMasksToBounds(true)

            val badgeLabel = UILabel()
            badgeLabel.setFrame(platform.CoreGraphics.CGRectMake(0.0, 0.0, badgeSize, badgeSize))
            badgeLabel.setText(marker.itemsCount.toString())
            badgeLabel.setTextColor(UIColor.whiteColor)
            badgeLabel.setTextAlignment(platform.UIKit.NSTextAlignmentCenter)
            badgeLabel.setFont(platform.UIKit.UIFont.systemFontOfSize(10.0))
            badgeView.addSubview(badgeLabel)

            containerView.addSubview(badgeView)
        }

        annotationView.setFrame(platform.CoreGraphics.CGRectMake(0.0, 0.0, markerSize, markerSize))
        annotationView.addSubview(containerView)
        annotationView.setCenterOffset(platform.CoreGraphics.CGPointMake(0.0, -markerSize / 2.0))

        return annotationView
    }

    override fun mapView(
        mapView: MKMapView,
        viewForAnnotation: MKAnnotationProtocol
    ): MKAnnotationView? {
        if (viewForAnnotation is MarkerAnnotation) {
            val identifier = "CustomMerchantMarker_${viewForAnnotation.marker.id}"
            
            // Always create a fresh view to avoid reuse issues
            val annotationView = createCustomAnnotationView(viewForAnnotation, identifier)
            return annotationView
        }
        return null
    }

    override fun mapView(mapView: MKMapView, didSelectAnnotationView: MKAnnotationView) {
        val annotation = didSelectAnnotationView.annotation
        if (annotation is MarkerAnnotation) {
            onMarkerInfoClick?.invoke(annotation.marker)
            mapView.deselectAnnotation(annotation, animated = false)
        }
    }
}

@OptIn(ExperimentalObjCName::class)
@ObjCName("MapTapHandler")
class MapTapHandler(
    private val mapView: MKMapView
) : NSObject() {
    private var onTap: ((Double, Double) -> Unit)? = null

    fun setOnTap(callback: ((Double, Double) -> Unit)?) {
        onTap = callback
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    @ObjCAction
    fun handleTap(gesture: UITapGestureRecognizer) {
        if (gesture.state == UIGestureRecognizerStateEnded) {
            val callback = onTap ?: return
            val point = gesture.locationInView(mapView)
            mapView.convertPoint(
                point,
                toCoordinateFromView = mapView
            ).useContents {
                callback(latitude, longitude)
            }
        }
    }
}


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
    val delegate = remember(onMarkerInfoClick) {
        MapViewDelegate(onMarkerInfoClick)
    }

    Box(modifier = modifier) {
        UIKitView(
            factory = {
                val mapView = MKMapView()
                mapView.mapType = MKMapTypeStandard
                mapView.delegate = delegate

                if (onMapClick != null) {
                    val mapTapHandler = MapTapHandler(mapView).also {
                        it.setOnTap { lat, long ->
                            onMapClick(LatLong(lat, long))
                        }
                    }

                    val tapGesture = UITapGestureRecognizer(
                        target = mapTapHandler,
                        action = NSSelectorFromString("handleTap:")
                    )
                    mapView.addGestureRecognizer(tapGesture)
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
                // Remove only custom marker annotations (not user location)
                val customAnnotations = mapView.annotations.filterIsInstance<MarkerAnnotation>()
                mapView.removeAnnotations(customAnnotations)
                
                // Add markers
                markers?.forEach { marker ->
                    if (marker.isVisible) {
                        val annotation = MarkerAnnotation(marker)
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