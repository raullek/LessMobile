package az.less.mobile.presentation.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import az.less.mobile.presentation.maps.models.CameraPosition
import az.less.mobile.presentation.maps.models.LatLong
import az.less.mobile.presentation.maps.models.MapType
import az.less.mobile.presentation.maps.models.Marker
import io.ktor.websocket.Frame

/**
 * Sample screen demonstrating GoogleMaps usage
 * 
 * This screen shows how to use the cross-platform GoogleMaps composable
 * with various features like markers, camera positioning, and user interactions.
 */
@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    var selectedMarker by remember { mutableStateOf<Marker?>(null) }
    var isTrackingEnabled by remember { mutableStateOf(false) }
    
    // Sample markers in Baku
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

    Scaffold(
    ) { paddingValues ->
        GoogleMaps(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            
            // Initial camera position centered on Baku
            shouldSetInitialCameraPosition = CameraPosition(
                target = LatLong(40.4093, 49.8671),
                zoom = 12f
            ),
            
            // Map settings
            mapType = MapType.NORMAL,
            isMapOptionSwitchesVisible = true,
            isZoomControlsVisible = true,
            isCompassVisible = true,
            
            // Markers
            markers = markers,
            onMarkerInfoClick = { marker ->
                selectedMarker = marker
                println("Marker clicked: ${marker.title}")
            },
            
            // Map interactions
            onMapClick = { latLong ->
                println("Map clicked at: ${latLong.latitude}, ${latLong.longitude}")
            },
            onMapLongClick = { latLong ->
                println("Map long clicked at: ${latLong.latitude}, ${latLong.longitude}")
            },
            
            // Location tracking
            isTrackingEnabled = isTrackingEnabled,
            onFindMeButtonClick = {
                isTrackingEnabled = true
                println("Find me clicked")
            },
            
            // Polyline example (route between markers)
            polyLine = listOf(
                LatLong(40.4093, 49.8671),
                LatLong(40.3629, 49.8352),
                LatLong(40.3663, 49.8351)
            ),
            polyLineColor = 0xFF0000FF, // Blue
            polyLineWidth = 5f
        )
    }
}


