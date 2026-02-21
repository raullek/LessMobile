package az.less.mobile.presentation.client.main.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import az.less.mobile.domain.repository.FavoritesRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.presentation.client.main.favorites.models.FavoriteMerchant
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import dev.jordond.compass.permissions.LocationPermissionController
import dev.jordond.compass.permissions.mobile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val sessionLocalRepository: SessionLocalRepository
) : ViewModel(), ContainerHost<FavoritesState, FavoritesSideEffect> {

    override val container: Container<FavoritesState, FavoritesSideEffect> =
        viewModelScope.container(FavoritesState())

    private val geolocator: Geolocator = Geolocator.mobile()
    private val permissionController: LocationPermissionController =
        LocationPermissionController.mobile()

    private val locationParams = MutableStateFlow<LocationParams?>(null)

    val favorites: Flow<PagingData<FavoriteMerchant>> = locationParams
        .flatMapLatest { params ->
            if (params == null) {
                flowOf(PagingData.empty())
            } else {
                favoritesRepository.getFavorites(
                    latitude = params.latitude,
                    longitude = params.longitude
                )
            }
        }
        .cachedIn(viewModelScope)

    init {
        observeSession()
    }

    fun onIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.OnMerchantClicked -> handleMerchantClicked(intent.merchantId)
            is FavoritesIntent.OnExploreNewVenuesClicked -> handleExploreNewVenuesClicked()
            is FavoritesIntent.OnSignInClicked -> handleSignInClicked()
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            sessionLocalRepository.isLoggedIn.collect { loggedIn ->
                intent { reduce { state.copy(isLoggedIn = loggedIn) } }
                if (loggedIn) {
                    fetchLocation()
                }
            }
        }
    }

    private fun fetchLocation() {
        viewModelScope.launch {
            val location = withTimeoutOrNull(3000L) {
                val hasPermission = permissionController.hasPermission()
                val isAvailable = geolocator.isAvailable()
                if (hasPermission && isAvailable) geolocator.current().getOrNull() else null
            }
            locationParams.value = LocationParams(
                latitude = location?.coordinates?.latitude,
                longitude = location?.coordinates?.longitude
            )
        }
    }

    private fun handleMerchantClicked(merchantId: String) = intent {
        postSideEffect(FavoritesSideEffect.NavigateToMerchantDetail(merchantId))
    }

    private fun handleExploreNewVenuesClicked() = intent {
        postSideEffect(FavoritesSideEffect.NavigateToExplore)
    }

    private fun handleSignInClicked() = intent {
        postSideEffect(FavoritesSideEffect.NavigateToMore)
    }
}

private data class LocationParams(
    val latitude: Double? = null,
    val longitude: Double? = null
)
