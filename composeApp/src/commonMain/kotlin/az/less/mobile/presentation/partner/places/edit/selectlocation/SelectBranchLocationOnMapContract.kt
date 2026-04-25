package az.less.mobile.presentation.partner.places.edit.selectlocation

import az.less.mobile.presentation.maps.models.LatLong

/**
 * State for the Select Branch Location on Map screen
 */
data class SelectBranchLocationOnMapState(
    val selectedLocation: LatLong? = null,
    val selectedAddress: String = "",
    val initialLocation: LatLong? = null,
    val isLoading: Boolean = false,
    val isConfirmEnabled: Boolean = false
)

/**
 * Side Effects for navigation and one-time events
 */
sealed interface SelectBranchLocationOnMapSideEffect {
    data object NavigateBack : SelectBranchLocationOnMapSideEffect
    data class LocationSelected(val location: LatLong, val address: String) : SelectBranchLocationOnMapSideEffect
    data object NavigateToInputAddress : SelectBranchLocationOnMapSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface SelectBranchLocationOnMapIntent {
    data object OnBackClick : SelectBranchLocationOnMapIntent
    data class OnMapClick(val latLong: LatLong) : SelectBranchLocationOnMapIntent
    data object OnConfirmClick : SelectBranchLocationOnMapIntent
    data object OnMyLocationClick : SelectBranchLocationOnMapIntent
    data object OnAddressCardClick : SelectBranchLocationOnMapIntent
    data class OnAddressInputResult(val address: String, val latitude: Double, val longitude: Double) : SelectBranchLocationOnMapIntent
}
