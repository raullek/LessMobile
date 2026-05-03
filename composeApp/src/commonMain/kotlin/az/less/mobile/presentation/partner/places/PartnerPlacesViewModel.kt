package az.less.mobile.presentation.partner.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.presentation.partner.places.model.BranchItem
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Merchant Places Screen using Orbit MVI
 */
class PartnerPlacesViewModel(
    private val venuesRepository: VenuesRepository
) : ViewModel(), ContainerHost<PartnerPlacesState, PartnerPlacesSideEffect> {

    override val container: Container<PartnerPlacesState, PartnerPlacesSideEffect> =
        viewModelScope.container(PartnerPlacesState())

    init {
        loadBranches()
    }

    private fun loadBranches(isRefresh: Boolean = false) = intent {
        reduce {
            if (isRefresh) state.copy(isRefreshing = true)
            else state.copy(isLoading = true)
        }

        venuesRepository.getAllVenues(page = 1)
            .onSuccess { data ->
                val branches = data.data.map { it.toBranchItem() }
                reduce {
                    state.copy(
                        branches = branches,
                        isLoading = false,
                        isRefreshing = false,
                        currentPage = 1,
                        hasNextPage = data.pagination?.hasNext ?: false
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false, isRefreshing = false) }
                postSideEffect(PartnerPlacesSideEffect.ShowError(error.message))
            }
    }

    private fun loadMore() = intent {
        if (state.isLoadingMore || !state.hasNextPage) return@intent

        val nextPage = state.currentPage + 1
        reduce { state.copy(isLoadingMore = true) }

        venuesRepository.getAllVenues(page = nextPage)
            .onSuccess { data ->
                val newBranches = data.data.map { it.toBranchItem() }
                reduce {
                    state.copy(
                        branches = state.branches + newBranches,
                        isLoadingMore = false,
                        currentPage = nextPage,
                        hasNextPage = data.pagination?.hasNext ?: false
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoadingMore = false) }
                postSideEffect(PartnerPlacesSideEffect.ShowError(error.message))
            }
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: PartnerPlacesIntent) {
        when (intent) {
            is PartnerPlacesIntent.OnBackClick -> handleBackClick()
            is PartnerPlacesIntent.OnBranchClick -> handleBranchClick(intent.branchId)
            is PartnerPlacesIntent.OnEditBranchClick -> handleEditBranchClick(intent.branchId)
            is PartnerPlacesIntent.OnAddBranchClick -> handleAddBranchClick()
            is PartnerPlacesIntent.OnRefresh -> loadBranches(isRefresh = true)
            is PartnerPlacesIntent.OnLoadMore -> loadMore()
            is PartnerPlacesIntent.OnDismissEditBottomSheet -> dismissEditBottomSheet()
            is PartnerPlacesIntent.OnEditVenueClick -> handleEditVenueFromBottomSheet()
            is PartnerPlacesIntent.OnEditUsersClick -> handleEditUsersFromBottomSheet()
            is PartnerPlacesIntent.OnPreviewClick -> handlePreviewFromBottomSheet()
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(PartnerPlacesSideEffect.NavigateBack)
    }

    private fun handleBranchClick(branchId: String) = intent {
        reduce { state.copy(selectedBranchId = branchId, showEditBottomSheet = true) }
    }

    private fun handleEditBranchClick(branchId: String) = intent {
        val branch = state.branches.find { it.id == branchId } ?: return@intent
        postSideEffect(PartnerPlacesSideEffect.NavigateToEditBranch(branch))
    }

    private fun handleAddBranchClick() = intent {
        postSideEffect(PartnerPlacesSideEffect.NavigateToAddBranch)
    }

    private fun dismissEditBottomSheet() = intent {
        reduce { state.copy(showEditBottomSheet = false, selectedBranchId = null) }
    }

    private fun handleEditVenueFromBottomSheet() = intent {
        val branch = state.branches.find { it.id == state.selectedBranchId } ?: return@intent
        reduce { state.copy(showEditBottomSheet = false, selectedBranchId = null) }
        postSideEffect(PartnerPlacesSideEffect.NavigateToEditBranch(branch))
    }

    private fun handleEditUsersFromBottomSheet() = intent {
        val branch = state.branches.find { it.id == state.selectedBranchId } ?: return@intent
        reduce { state.copy(showEditBottomSheet = false, selectedBranchId = null) }
        postSideEffect(PartnerPlacesSideEffect.NavigateToEditUsers(branch))
    }

    private fun handlePreviewFromBottomSheet() = intent {
        val branch = state.branches.find { it.id == state.selectedBranchId } ?: return@intent
        reduce { state.copy(showEditBottomSheet = false, selectedBranchId = null) }
        postSideEffect(PartnerPlacesSideEffect.NavigateToPreview(branch))
    }
}

private fun az.less.mobile.data.remote.model.AdminVenueDto.toBranchItem(): BranchItem {
    return BranchItem(
        id = id,
        name = name,
        address = businessAddress ?: "",
        phone = phone ?: "",
        imageUrl = coverImage,
        logoUrl = businessLogo,
        lotImageUrl = lotImage,
        itemsOnSale = boxCounts?.available ?: 0,
        hasActiveDiscount = (boxCounts?.available ?: 0) > 0,
        status = status,
        businessName = businessName,
        businessDescription = businessDescription,
        defaultBoxDescription = defaultBoxDescription,
        email = email,
        latitude = location?.coordinates?.getOrNull(1),
        longitude = location?.coordinates?.getOrNull(0)
    )
}
