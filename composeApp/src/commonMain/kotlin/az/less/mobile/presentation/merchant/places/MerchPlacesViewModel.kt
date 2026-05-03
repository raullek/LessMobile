package az.less.mobile.presentation.merchant.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.presentation.merchant.places.model.BranchItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Merchant Places Screen using Orbit MVI
 */
class MerchPlacesViewModel(
    private val venuesRepository: VenuesRepository,
    private val sessionLocalRepository: SessionLocalRepository
) : ViewModel(), ContainerHost<MerchPlacesState, MerchPlacesSideEffect> {

    override val container: Container<MerchPlacesState, MerchPlacesSideEffect> =
        viewModelScope.container(MerchPlacesState())

    init {
        observeCurrentUserVenue()
        loadBranches()
    }

    private fun observeCurrentUserVenue() {
        viewModelScope.launch {
            sessionLocalRepository.currentUser.collectLatest { user ->
                intent {
                    // Tag the default venue only when this user actually administers branches
                    // (CMP / merchant-partner). For pure merchant or pure partner the server
                    // does not return `user.venue`, so this is also a safety net.
                    val defaultId = user?.takeIf { it.canManageBranches }?.venue?.id
                    reduce { state.copy(defaultVenueId = defaultId) }
                }
            }
        }
    }

    private fun loadBranches() = intent {
        reduce { state.copy(isLoading = true) }

        venuesRepository.getAllVenues(page = 1)
            .onSuccess { data ->
                val branches = data.data.map { it.toBranchItem() }
                reduce {
                    state.copy(
                        branches = branches,
                        isLoading = false,
                        currentPage = 1,
                        hasNextPage = data.pagination?.hasNext ?: false
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(MerchPlacesSideEffect.ShowError(error.message))
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
                postSideEffect(MerchPlacesSideEffect.ShowError(error.message))
            }
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: MerchPlacesIntent) {
        when (intent) {
            is MerchPlacesIntent.OnBackClick -> handleBackClick()
            is MerchPlacesIntent.OnBranchClick -> handleBranchClick(intent.branchId)
            is MerchPlacesIntent.OnEditBranchClick -> handleEditBranchClick(intent.branchId)
            is MerchPlacesIntent.OnAddBranchClick -> handleAddBranchClick()
            is MerchPlacesIntent.OnLoadMore -> loadMore()
            is MerchPlacesIntent.OnDismissEditBottomSheet -> dismissEditBottomSheet()
            is MerchPlacesIntent.OnEditVenueClick -> handleEditVenueFromBottomSheet()
            is MerchPlacesIntent.OnEditUsersClick -> handleEditUsersFromBottomSheet()
            is MerchPlacesIntent.OnPreviewClick -> handlePreviewFromBottomSheet()
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(MerchPlacesSideEffect.NavigateBack)
    }

    private fun handleBranchClick(branchId: String) = intent {
        reduce { state.copy(selectedBranchId = branchId, showEditBottomSheet = true) }
    }

    private fun handleEditBranchClick(branchId: String) = intent {
        val branch = state.branches.find { it.id == branchId } ?: return@intent
        postSideEffect(MerchPlacesSideEffect.NavigateToEditBranch(branch))
    }

    private fun handleAddBranchClick() = intent {
        postSideEffect(MerchPlacesSideEffect.NavigateToAddBranch)
    }

    private fun dismissEditBottomSheet() = intent {
        reduce { state.copy(showEditBottomSheet = false, selectedBranchId = null) }
    }

    private fun handleEditVenueFromBottomSheet() = intent {
        val branch = state.branches.find { it.id == state.selectedBranchId } ?: return@intent
        reduce { state.copy(showEditBottomSheet = false, selectedBranchId = null) }
        postSideEffect(MerchPlacesSideEffect.NavigateToEditBranch(branch))
    }

    private fun handleEditUsersFromBottomSheet() = intent {
        val branch = state.branches.find { it.id == state.selectedBranchId } ?: return@intent
        reduce { state.copy(showEditBottomSheet = false, selectedBranchId = null) }
        postSideEffect(MerchPlacesSideEffect.NavigateToEditUsers(branch))
    }

    private fun handlePreviewFromBottomSheet() = intent {
        val branch = state.branches.find { it.id == state.selectedBranchId } ?: return@intent
        reduce { state.copy(showEditBottomSheet = false, selectedBranchId = null) }
        postSideEffect(MerchPlacesSideEffect.NavigateToPreview(branch))
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
