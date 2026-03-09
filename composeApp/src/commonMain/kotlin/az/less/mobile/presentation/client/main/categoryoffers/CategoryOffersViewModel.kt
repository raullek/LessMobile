package az.less.mobile.presentation.client.main.categoryoffers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import az.less.mobile.domain.model.FilterBox
import az.less.mobile.domain.model.toOfferItem
import az.less.mobile.domain.repository.ExploreRepository
import az.less.mobile.presentation.client.main.offers.models.OfferItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class CategoryOffersViewModel(
    private val exploreRepository: ExploreRepository
) : ViewModel(), ContainerHost<CategoryOffersState, CategoryOffersSideEffect> {

    override val container: Container<CategoryOffersState, CategoryOffersSideEffect> =
        viewModelScope.container(CategoryOffersState())

    private var isInitialized = false

    var boxesPagingFlow: Flow<PagingData<OfferItem>> = emptyFlow()
        private set

    fun initialize(categoryTitle: String, filtersJson: String) {
        if (isInitialized) return
        isInitialized = true

        boxesPagingFlow = exploreRepository.searchBoxesByFiltersPaged(
            filtersJson = filtersJson,
            latitude = null,
            longitude = null
        ).map { pagingData ->
            pagingData.map { it.toOfferItem() }
        }.cachedIn(viewModelScope)

        intent {
            reduce {
                state.copy(
                    categoryTitle = categoryTitle,
                    filtersJson = filtersJson,
                    isLoading = false
                )
            }
        }
    }

    fun onIntent(intent: CategoryOffersIntent) {
        when (intent) {
            is CategoryOffersIntent.OnBackClicked -> handleBackClicked()
            is CategoryOffersIntent.OnOfferItemClicked -> handleOfferItemClicked(intent.offerId)
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(CategoryOffersSideEffect.NavigateBack)
    }

    private fun handleOfferItemClicked(offerId: String) = intent {
        // Will be handled via paging items in the screen
    }
}
