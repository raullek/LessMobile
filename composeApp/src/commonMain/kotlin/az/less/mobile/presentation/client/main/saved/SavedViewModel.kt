package az.less.mobile.presentation.client.main.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.client.main.saved.models.SavedMerchant
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Saved/Favorites Screen using Orbit MVI
 */
class SavedViewModel : ViewModel(), ContainerHost<SavedState, SavedSideEffect> {

    override val container: Container<SavedState, SavedSideEffect> =
        viewModelScope.container(SavedState())

    init {
        loadSavedMerchants()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: SavedIntent) {
        when (intent) {
            is SavedIntent.OnMerchantClicked -> handleMerchantClicked(intent.merchantId)
            is SavedIntent.OnRemoveFromFavorites -> handleRemoveFromFavorites(intent.merchantId)
            is SavedIntent.OnExploreNewVenuesClicked -> handleExploreNewVenuesClicked()
        }
    }

    private fun loadSavedMerchants() = intent {
        reduce { state.copy(isLoading = true) }

        // Mock network delay for shimmer loading demonstration
        delay(2000L)

        reduce {
            state.copy(
                savedMerchants = getMockSavedMerchants(),
                isLoading = false
            )
        }
    }

    private fun handleMerchantClicked(merchantId: String) = intent {
        postSideEffect(SavedSideEffect.NavigateToMerchantDetail(merchantId))
    }

    private fun handleRemoveFromFavorites(merchantId: String) = intent {
        reduce {
            state.copy(
                savedMerchants = state.savedMerchants.filter { it.id != merchantId }
            )
        }
    }

    private fun handleExploreNewVenuesClicked() = intent {
        postSideEffect(SavedSideEffect.NavigateToExplore)
    }

    // Mock data - replace with repository calls in real app
    private fun getMockSavedMerchants(): List<SavedMerchant> {
        // Return empty list for empty state
        // return emptyList()

        // Return merchants for list view
        return listOf(
            SavedMerchant(
                id = "1",
                merchantName = "Belgian Chocolate & Coffee",
                address = "Adres will be here",
                rating = 4.9f,
                distance = "1.2 km",
                itemsOnSale = 12
            ),
            SavedMerchant(
                id = "2",
                merchantName = "Belgian Chocolate & Coffee",
                address = "Adres will be here",
                rating = 4.9f,
                distance = "1.2 km",
                itemsOnSale = 0 // No active offer
            ),
            SavedMerchant(
                id = "3",
                merchantName = "Belgian Chocolate & Coffee",
                address = "Adres will be here",
                rating = 4.9f,
                distance = "1.2 km",
                itemsOnSale = 12
            ),
            SavedMerchant(
                id = "4",
                merchantName = "Belgian Chocolate & Coffee",
                address = "Adres will be here",
                rating = 4.9f,
                distance = "1.2 km",
                itemsOnSale = 12
            )
        )
    }
}
