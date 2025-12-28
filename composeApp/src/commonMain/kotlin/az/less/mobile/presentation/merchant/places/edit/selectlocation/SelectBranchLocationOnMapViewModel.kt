package az.less.mobile.presentation.merchant.places.edit.selectlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.maps.models.LatLong
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Select Branch Location on Map Screen using Orbit MVI
 */
class SelectBranchLocationOnMapViewModel : ViewModel(), ContainerHost<SelectBranchLocationOnMapState, SelectBranchLocationOnMapSideEffect> {

    override val container: Container<SelectBranchLocationOnMapState, SelectBranchLocationOnMapSideEffect> =
        viewModelScope.container(SelectBranchLocationOnMapState())

    // Default location: Baku, Azerbaijan
    private val defaultLocation = LatLong(40.4093, 49.8671)

    fun initialize(initialLatitude: Double?, initialLongitude: Double?) = intent {
        // Check if we have initial coordinates passed in
        val hasInitialCoordinates = initialLatitude != null && initialLongitude != null
        val initialLocation = if (hasInitialCoordinates) {
            LatLong(initialLatitude, initialLongitude)
        } else {
            defaultLocation
        }

        reduce {
            state.copy(
                initialLocation = initialLocation,
                // Only set selected location if coordinates were passed in
                selectedLocation = if (hasInitialCoordinates) initialLocation else null,
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
        reduce {
            state.copy(
                selectedLocation = latLong,
                selectedAddress = formatAddress(latLong),
                isConfirmEnabled = true
            )
        }
    }

    private fun handleConfirmClick() = intent {
        val location = state.selectedLocation ?: return@intent
        postSideEffect(
            SelectBranchLocationOnMapSideEffect.LocationSelected(
                location = location,
                address = state.selectedAddress.ifEmpty { formatAddress(location) }
            )
        )
    }

    private fun handleMyLocationClick() = intent {
        // TODO: Implement user location tracking when permission is granted
        // For now, center on default location
        reduce {
            state.copy(
                selectedLocation = defaultLocation,
                selectedAddress = formatAddress(defaultLocation),
                isConfirmEnabled = true
            )
        }
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

    private fun formatAddress(latLong: LatLong): String {
        // TODO: Implement reverse geocoding to get actual address
        // For now, return formatted coordinates
        return "%.4f, %.4f".format(latLong.latitude, latLong.longitude)
    }
}
