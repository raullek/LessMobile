package az.less.mobile.presentation.main.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.main.saved.models.FavoriteItem
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
        loadFavorites()
    }
    
    /**
     * Handle user intents
     */
    fun onIntent(intent: SavedIntent) {
        when (intent) {
            is SavedIntent.OnItemClicked -> handleItemClicked(intent.itemId)
            is SavedIntent.OnRemoveFromFavorites -> handleRemoveFromFavorites(intent.itemId)
            is SavedIntent.OnExploreNewVenuesClicked -> handleExploreNewVenuesClicked()
        }
    }
    
    private fun loadFavorites() = intent {
        reduce {
            state.copy(
                favoriteItems = getMockFavoriteItems(),
                isLoading = false
            )
        }
    }
    
    private fun handleItemClicked(itemId: String) = intent {
        postSideEffect(SavedSideEffect.NavigateToItemDetail(itemId))
    }
    
    private fun handleRemoveFromFavorites(itemId: String) = intent {
        reduce {
            state.copy(
                favoriteItems = state.favoriteItems.filter { it.id != itemId }
            )
        }
    }
    
    private fun handleExploreNewVenuesClicked() = intent {
        postSideEffect(SavedSideEffect.NavigateToExplore)
    }
    
    // Mock data - replace with repository calls in real app
    // Return empty list to show empty state, or return items for list view
    private fun getMockFavoriteItems(): List<FavoriteItem> {
        // Return empty list for empty state
        // return emptyList()
        
        // Return items for list view
        return listOf(
            FavoriteItem(
                id = "1",
                title = "Mixed donut bag",
                price = "12.99",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                reviewCount = "28+",
                distance = "1.2 km away"
            ),
            FavoriteItem(
                id = "2",
                title = "Mixed donut bag",
                price = "12.99",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                reviewCount = "28+",
                distance = "1.2 km away"
            ),
            FavoriteItem(
                id = "3",
                title = "Mixed donut bag",
                price = "12.99",
                pickupTime = "Pick up from 17:00 to 23:00",
                rating = 4.9f,
                reviewCount = "28+",
                distance = "1.2 km away"
            )
        )
    }
}

