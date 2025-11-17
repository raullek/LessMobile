package az.less.mobile.presentation.main.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.maps.GoogleMaps
import az.less.mobile.presentation.maps.LocationPermissionHandler
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker

@Composable
fun ExploreScreen() {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var locationPermissionDenied by remember { mutableStateOf(false) }
    
    // Sample markers for Baku
    val markers = remember {
        listOf(
            Marker(
                id = "1",
                position = LatLong(40.4093, 49.8671),
                title = "Baku City Center",
                snippet = "Capital of Azerbaijan"
            ),
            Marker(
                id = "2",
                position = LatLong(40.3629, 49.8352),
                title = "Heydar Aliyev Center",
                snippet = "Famous architectural landmark"
            ),
            Marker(
                id = "3",
                position = LatLong(40.3663, 49.8351),
                title = "Flame Towers",
                snippet = "Iconic buildings in Baku"
            )
        )
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
        Box(modifier = Modifier.fillMaxSize()) {
            // Map fills the entire screen
            GoogleMaps(
                modifier = Modifier.fillMaxSize(),
                shouldSetInitialCameraPosition = CameraPosition(
                    target = LatLong(40.4093, 49.8671), // Center on Baku
                    zoom = 12f
                ),
                mapType = MapType.NORMAL,
                isZoomControlsVisible = true,
                isCompassVisible = true,
                isTrackingEnabled = locationPermissionGranted,
                markers = markers,
                onMarkerInfoClick = { marker ->
                    println("Marker clicked: ${marker.title}")
                },
                onMapClick = { latLong ->
                    println("Map clicked at: ${latLong.latitude}, ${latLong.longitude}")
                },
                onFindMeButtonClick = if (locationPermissionGranted) {
                    { println("Find me clicked - location enabled") }
                } else null
            )
        }
    }
}

