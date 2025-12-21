package az.less.mobile.presentation.merchant.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.merchant.places.model.BranchItem
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Merchant Places Screen using Orbit MVI
 */
class MerchPlacesViewModel : ViewModel(), ContainerHost<MerchPlacesState, MerchPlacesSideEffect> {

    override val container: Container<MerchPlacesState, MerchPlacesSideEffect> =
        viewModelScope.container(MerchPlacesState())

    init {
        loadBranches()
    }

    private fun loadBranches() = intent {
        reduce { state.copy(isLoading = true) }

        // TODO: Load branches from repository
        // For now, using mock data
        val mockBranches = listOf<BranchItem>(
//            BranchItem(
//                id = "1",
//                name = "Belgian Chocolate & Coffee",
//                address = "Adres will be here",
//                phone = "+994 12 123 45 67",
//                itemsOnSale = 12,
//                hasActiveDiscount = true,
//                rating = 4.9f,
//                distance = "1.2 km"
//            ),
//            BranchItem(
//                id = "2",
//                name = "McDonald's Ganjlik",
//                address = "Ganjlik Mall, Baku, Azerbaijan",
//                phone = "+994 12 234 56 78",
//                itemsOnSale = 0,
//                hasActiveDiscount = false,
//                rating = 4.7f,
//                distance = "2.5 km"
//            ),
//            BranchItem(
//                id = "3",
//                name = "McDonald's 28 May",
//                address = "28 May metro station, Baku, Azerbaijan",
//                phone = "+994 12 345 67 89",
//                itemsOnSale = 12,
//                hasActiveDiscount = true,
//                rating = 4.8f,
//                distance = "0.8 km"
//            )
        )

        reduce {
            state.copy(
                branches = mockBranches,
                isLoading = false
            )
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
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(MerchPlacesSideEffect.NavigateBack)
    }

    private fun handleBranchClick(branchId: String) = intent {
        // Navigate to branch details or edit
        postSideEffect(MerchPlacesSideEffect.NavigateToEditBranch(branchId))
    }

    private fun handleEditBranchClick(branchId: String) = intent {
        postSideEffect(MerchPlacesSideEffect.NavigateToEditBranch(branchId))
    }

    private fun handleAddBranchClick() = intent {
        postSideEffect(MerchPlacesSideEffect.NavigateToAddBranch)
    }
}
