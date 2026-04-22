package az.less.mobile.presentation.partner.places.edit.selectlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.maps.models.LatLong
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.mobile
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Select Branch Location on Map Screen using Orbit MVI
 */
class SelectBranchLocationOnMapViewModel() : ViewModel(), ContainerHost<SelectBranchLocationOnMapState, SelectBranchLocationOnMapSideEffect> {

    override val container: Container<SelectBranchLocationOnMapState, SelectBranchLocationOnMapSideEffect> =
        viewModelScope.container(SelectBranchLocationOnMapState())

    // Default location: Baku, Azerbaijan
    private val defaultLocation = LatLong(40.4093, 49.8671)
    private val geocoder: Geocoder = Geocoder.mobile()

    /**
     * Initialize ViewModel with initial coordinates and address
     */
    fun initialize(initialLatitude: Double?, initialLongitude: Double?, initialAddress: String? = null) = intent {
        val hasInitialCoordinates = initialLatitude != null && initialLongitude != null
        val initialLocation = if (hasInitialCoordinates) {
            LatLong(initialLatitude!!, initialLongitude!!)
        } else {
            defaultLocation
        }

        reduce {
            state.copy(
                initialLocation = initialLocation,
                // Only set selected location if coordinates were passed in
                selectedLocation = if (hasInitialCoordinates) initialLocation else null,
                selectedAddress = initialAddress ?: "",
                isConfirmEnabled = hasInitialCoordinates
            )
        }
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: SelectBranchLocationOnMapIntent) {
        when (intent) {
            is SelectBranchLocationOnMapIntent.OnBackClick -> handleBackClick()
            is SelectBranchLocationOnMapIntent.OnMapClick -> handleMapClick(intent.latLong)
              is SelectBranchLocationOnMapIntent.OnConfirmClick -> handleConfirmClick()
            is SelectBranchLocationOnMapIntent.OnMyLocationClick -> handleMyLocationClick()
            is SelectBranchLocationOnMapIntent.OnAddressCardClick -> handleAddressCardClick()
            is SelectBranchLocationOnMapIntent.OnAddressInputResult -> handleAddressInputResult(intent.address, intent.latitude, intent.longitude)
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(SelectBranchLocationOnMapSideEffect.NavigateBack)
    }

    private fun handleMapClick(latLong: LatLong) = intent {
        viewModelScope.launch {
            val places = geocoder.places(latLong.latitude, latLong.longitude)
            println(places)
            reduce {
                val adressName = "${places.getFirstOrNull()?.street}".ifEmpty {"${latLong.latitude}, ${latLong.longitude}"  }
                state.copy(
                    selectedLocation = latLong,
                    selectedAddress = adressName,
                    isConfirmEnabled = true
                )
            }
        }
    }

    private fun handleConfirmClick() = intent {
        val location = state.selectedLocation ?: return@intent
        postSideEffect(
            SelectBranchLocationOnMapSideEffect.LocationSelected(
                location = location,
                address = state.selectedAddress.ifEmpty { "${location.latitude}, ${location.longitude}" }
            )
        )
    }

    private fun handleMyLocationClick() = intent {
        // TODO: Implement user location tracking when permission is granted
        // For now, center on default location
//        reduce {
//            state.copy(
//                selectedLocation = defaultLocation,
//                selectedAddress = formatAddress(defaultLocation),
//                isConfirmEnabled = true
//            )
//        }
    }

    private fun handleAddressCardClick() = intent {
        postSideEffect(SelectBranchLocationOnMapSideEffect.NavigateToInputAddress)
    }

    private fun handleAddressInputResult(address: String, latitude: Double, longitude: Double) = intent {
        val newLocation = LatLong(latitude, longitude)
        reduce {
            state.copy(
                selectedLocation = newLocation,
                initialLocation = newLocation, // Update initial location to move camera
                selectedAddress = address,
                isConfirmEnabled = true
            )
        }
    }

}
