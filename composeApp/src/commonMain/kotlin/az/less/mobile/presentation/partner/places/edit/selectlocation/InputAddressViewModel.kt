package az.less.mobile.presentation.partner.places.edit.selectlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jordond.compass.Place
import dev.jordond.compass.autocomplete.Autocomplete
import dev.jordond.compass.autocomplete.AutocompleteResult
import dev.jordond.compass.autocomplete.mobile
import dev.jordond.compass.autocomplete.mobile.mobile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Input Address Screen using Orbit MVI
 * Uses Compass library for address autocomplete with Kotlin Flow
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class InputAddressViewModel : ViewModel(), ContainerHost<InputAddressState, InputAddressSideEffect> {

    override val container: Container<InputAddressState, InputAddressSideEffect> =
        viewModelScope.container(InputAddressState())

    // Compass Autocomplete instance for mobile (Android/iOS)
    private val autocomplete = Autocomplete.mobile()

    // Search query flow for debouncing
    private val searchQueryFlow = MutableStateFlow("")

    // Debounce delay in milliseconds
    private val debounceDelay = 300L

    init {
        // Set up search flow with debounce - updates dynamically without loading state
        searchQueryFlow
            .debounce(debounceDelay)
            .distinctUntilChanged()
            .filter { it.length >= 3 }
            .flatMapLatest { query ->
                flow {
                    try {
                        val result = autocomplete.search(query)
                        when (result) {
                            is AutocompleteResult.Success -> {
                                println("Success: ${result.data}")
                                emit(SearchResult.Success(result.data))
                            }
                            is AutocompleteResult.Error -> {
                                println("Success: ${result.message}")
                                emit(SearchResult.Error(result.message))
                            }
                        }
                    } catch (e: Exception) {
                        emit(SearchResult.Error(e.message ?: "An error occurred while searching"))
                    }
                }
            }
            .onEach { searchResult ->
                handleSearchResult(searchResult)
            }
            .launchIn(viewModelScope)
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: InputAddressIntent) {
        when (intent) {
            is InputAddressIntent.OnBackClick -> handleBackClick()
            is InputAddressIntent.OnSearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is InputAddressIntent.OnAddressSelected -> handleAddressSelected(intent.suggestion)
            is InputAddressIntent.OnClearSearch -> handleClearSearch()
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(InputAddressSideEffect.NavigateBack)
    }

    private fun handleSearchQueryChanged(query: String) = intent {
        reduce { state.copy(searchQuery = query, errorMessage = null) }

        if (query.length >= 3) {
            searchQueryFlow.value = query
        } else {
            searchQueryFlow.value = ""
            reduce {
                state.copy(
                    searchResults = emptyList()
                )
            }
        }
    }

    private fun handleSearchResult(searchResult: SearchResult) = intent {
        when (searchResult) {
            is SearchResult.Success -> {
                val suggestions = searchResult.places.mapIndexed { index, place ->
                    AddressSuggestion(
                        id = "${index}_${place.coordinates.latitude}_${place.coordinates.longitude}",
                        address = "${place.name.orEmpty()} / ${place.street.orEmpty()} ",
                        subtitle = formatPlaceSubtitle(place),
                        latitude = place.coordinates.latitude,
                        longitude = place.coordinates.longitude
                    )
                }
                reduce {
                    state.copy(
                        searchResults = suggestions,
                        errorMessage = null
                    )
                }
            }
            is SearchResult.Error -> {
                reduce {
                    state.copy(
                        searchResults = emptyList(),
                        errorMessage = searchResult.message
                    )
                }
            }
        }
    }

    /**
     * Format place subtitle from available place data
     */
    private fun formatPlaceSubtitle(place: Place): String? {
        val parts = listOfNotNull(
            place.locality,
            place.administrativeArea,
            place.country
        )
        return if (parts.isNotEmpty()) parts.joinToString(", ") else null
    }

    private fun handleAddressSelected(suggestion: AddressSuggestion) = intent {
        postSideEffect(
            InputAddressSideEffect.AddressSelected(
                address = suggestion.address,
                latitude = suggestion.latitude,
                longitude = suggestion.longitude
            )
        )
    }

    private fun handleClearSearch() = intent {
        searchQueryFlow.value = ""
        reduce {
            state.copy(
                searchQuery = "",
                searchResults = emptyList(),
                errorMessage = null
            )
        }
    }

    /**
     * Internal sealed class for search result
     */
    private sealed class SearchResult {
        data class Success(val places: List<Place>) : SearchResult()
        data class Error(val message: String) : SearchResult()
    }
}
