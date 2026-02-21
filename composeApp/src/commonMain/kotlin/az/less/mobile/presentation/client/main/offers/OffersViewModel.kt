package az.less.mobile.presentation.client.main.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.OffersRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.client.main.offers.models.UserInfo
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class OffersViewModel(
    private val offersRepository: OffersRepository,
    private val sessionLocalRepository: SessionLocalRepository
) : ViewModel(), ContainerHost<OffersState, OffersSideEffect> {

    override val container: Container<OffersState, OffersSideEffect> =
        viewModelScope.container(OffersState())

    private val geolocator: Geolocator = Geolocator.mobile()

    init {
        observeUserInfo()
        loadOffers()
    }

    fun onIntent(intent: OffersIntent) {
        when (intent) {
            is OffersIntent.OnLocationPermissionChanged -> handleLocationPermissionChanged(intent.granted)
            is OffersIntent.OnSearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is OffersIntent.OnSearchClicked -> handleSearchClicked()
            is OffersIntent.OnCategorySelected -> handleCategorySelected(intent.categoryId)
            is OffersIntent.OnSpecialCategoryClicked -> handleSpecialCategoryClicked(intent.specialCategoryId)
            is OffersIntent.OnSegmentSelected -> handleSegmentSelected(intent.segmentId)
            is OffersIntent.OnOfferItemClicked -> handleOfferItemClicked(intent.offerId)
            is OffersIntent.OnSeeAllClicked -> handleSeeAllClicked(intent.sectionId)
            is OffersIntent.OnRefresh -> handleRefresh()
        }
    }

    private fun observeUserInfo() {
        viewModelScope.launch {
            sessionLocalRepository.currentUser.collectLatest { user ->
                intent {
                    reduce {
                        state.copy(
                            userInfo = user?.let {
                                UserInfo(
                                    id = it.id,
                                    name = it.name,
                                    avatarUrl = null
                                )
                            },
                            isUserLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun handleLocationPermissionChanged(granted: Boolean) = intent {
        reduce { state.copy(locationPermissionGranted = granted) }
        if (granted) {
            loadOffers()
        }
    }

    private suspend fun getLocation() = withTimeoutOrNull(3000L) {
        if (geolocator.isAvailable()) geolocator.current().getOrNull() else null
    }

    private fun loadOffers() = intent {
        val location = if (state.locationPermissionGranted) getLocation() else null
        val latitude = location?.coordinates?.latitude
        val longitude = location?.coordinates?.longitude

        offersRepository.getHomeOffers(
            latitude = latitude,
            longitude = longitude
        )
            .onSuccess { data ->
                reduce {
                    state.copy(
                        categories = data.categories,
                        specialCategories = data.specialCategories,
                        segmentedCategories = data.segmentedCategories,
                        offerSections = data.offerSections,
                        isLoading = false
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false) }
                postSideEffect(OffersSideEffect.ShowError(error.message))
            }
    }

    private fun handleSearchQueryChanged(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
    }

    private fun handleSearchClicked() = intent {
        postSideEffect(OffersSideEffect.NavigateToSearch)
    }

    private fun handleCategorySelected(categoryId: String) = intent {
        val category = state.categories.find { it.id == categoryId }
        if (category != null) {
            postSideEffect(
                OffersSideEffect.NavigateToCategoryOffers(
                    categoryId = category.id,
                    categoryType = category.type,
                    categoryTitle = category.title
                )
            )
        }
    }

    private fun handleSpecialCategoryClicked(specialCategoryId: String) = intent {
        val specialCategory = state.specialCategories.find { it.id == specialCategoryId }
        if (specialCategory != null) {
            postSideEffect(
                OffersSideEffect.NavigateToCategoryOffers(
                    categoryId = specialCategory.id,
                    categoryType = specialCategory.type,
                    categoryTitle = specialCategory.title
                )
            )
        }
    }

    private fun handleSegmentSelected(segmentId: String) = intent {
        val segment = state.segmentedCategories.find { it.id == segmentId }
        if (segment != null) {
            postSideEffect(
                OffersSideEffect.NavigateToCategoryOffers(
                    categoryId = segment.id,
                    categoryType = segment.type,
                    categoryTitle = segment.title
                )
            )
        }
    }

    private fun handleSeeAllClicked(sectionId: String) = intent {
        val section = state.offerSections.find { it.id == sectionId }
        if (section != null) {
            postSideEffect(
                OffersSideEffect.NavigateToCategoryOffers(
                    categoryId = section.id,
                    categoryType = section.type,
                    categoryTitle = section.title
                )
            )
        }
    }

    private fun handleRefresh() = intent {
        reduce { state.copy(isRefreshing = true) }

        val location = if (state.locationPermissionGranted) getLocation() else null
        val latitude = location?.coordinates?.latitude
        val longitude = location?.coordinates?.longitude

        offersRepository.getHomeOffers(
            latitude = latitude,
            longitude = longitude
        )
            .onSuccess { data ->
                reduce {
                    state.copy(
                        categories = data.categories,
                        specialCategories = data.specialCategories,
                        segmentedCategories = data.segmentedCategories,
                        offerSections = data.offerSections,
                        isRefreshing = false
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isRefreshing = false) }
                postSideEffect(OffersSideEffect.ShowError(error.message))
            }
    }

    private fun handleOfferItemClicked(offerId: String) = intent {
        postSideEffect(OffersSideEffect.NavigateToReserve)
    }
}
