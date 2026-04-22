package az.less.mobile.presentation.partner.places

import az.less.mobile.presentation.partner.places.model.BranchItem

/**
 * State of the Merchant Places Screen
 */
data class PartnerPlacesState(
    val branches: List<BranchItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val selectedBranchId: String? = null,
    val showEditBottomSheet: Boolean = false
) {
    val isEmpty: Boolean get() = branches.isEmpty() && !isLoading
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface PartnerPlacesSideEffect {
    data object NavigateBack : PartnerPlacesSideEffect
    data class NavigateToEditBranch(val branch: BranchItem) : PartnerPlacesSideEffect
    data class NavigateToEditUsers(val branch: BranchItem) : PartnerPlacesSideEffect
    data object NavigateToAddBranch : PartnerPlacesSideEffect
    data class ShowError(val message: String) : PartnerPlacesSideEffect
}

/**
 * User Intents/Actions
 */
sealed interface PartnerPlacesIntent {
    data object OnBackClick : PartnerPlacesIntent
    data class OnBranchClick(val branchId: String) : PartnerPlacesIntent
    data class OnEditBranchClick(val branchId: String) : PartnerPlacesIntent
    data object OnAddBranchClick : PartnerPlacesIntent
    data object OnLoadMore : PartnerPlacesIntent
    data object OnDismissEditBottomSheet : PartnerPlacesIntent
    data object OnEditVenueClick : PartnerPlacesIntent
    data object OnEditUsersClick : PartnerPlacesIntent
}
