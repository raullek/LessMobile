package az.less.mobile.presentation.merchant.places

import az.less.mobile.presentation.merchant.places.model.BranchItem

/**
 * State of the Merchant Places Screen
 */
data class MerchPlacesState(
    val branches: List<BranchItem> = emptyList(),
    val isLoading: Boolean = false
) {
    val isEmpty: Boolean get() = branches.isEmpty() && !isLoading
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface MerchPlacesSideEffect {
    data object NavigateBack : MerchPlacesSideEffect
    data class NavigateToEditBranch(val branchId: String) : MerchPlacesSideEffect
    data object NavigateToAddBranch : MerchPlacesSideEffect
    data class ShowError(val message: String) : MerchPlacesSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface MerchPlacesIntent {
    data object OnBackClick : MerchPlacesIntent
    data class OnBranchClick(val branchId: String) : MerchPlacesIntent
    data class OnEditBranchClick(val branchId: String) : MerchPlacesIntent
    data object OnAddBranchClick : MerchPlacesIntent
}
