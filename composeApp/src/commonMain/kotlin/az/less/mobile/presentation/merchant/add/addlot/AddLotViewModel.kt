package az.less.mobile.presentation.merchant.add.addlot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.ContentRepository
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.domain.usecase.ValidateAndBuildBoxRequestUseCase
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_DESCRIPTION
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_TAGS
import az.less.mobile.presentation.merchant.add.addlot.model.toAddLotResponseModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class AddLotViewModel(
    private val contentRepository: ContentRepository,
    private val venuesRepository: VenuesRepository,
    private val validateAndBuildBoxRequest: ValidateAndBuildBoxRequestUseCase
) : ViewModel(), ContainerHost<AddLotState, AddLotSideEffect> {

    override val container: Container<AddLotState, AddLotSideEffect> =
        viewModelScope.container(AddLotState())

    init {
        loadData()
    }

    private fun loadData() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        contentRepository.getFilterOptions()
            .onSuccess { data ->
                reduce {
                    state.copy(
                        responseModel = data.toAddLotResponseModel(),
                        isLoading = false,
                        boxCount = 1
                    )
                }
            }
            .onError { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
    }

    fun onIntent(intent: AddLotIntent) {
        when (intent) {
            is AddLotIntent.OnBackClick -> handleBackClick()
            is AddLotIntent.OnLoadData -> loadData()
            is AddLotIntent.OnSingleSelect -> handleSingleSelect(intent.sectionId, intent.optionId)
            is AddLotIntent.OnMultiSelectToggle -> handleMultiSelectToggle(intent.sectionId, intent.optionId)
            is AddLotIntent.OnInputChanged -> handleInputChanged(intent.fieldId, intent.value)
            is AddLotIntent.OnBoxCountIncrement -> handleBoxCountIncrement()
            is AddLotIntent.OnBoxCountDecrement -> handleBoxCountDecrement()
            is AddLotIntent.OnAddLotClick -> handleAddLotClick()
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(AddLotSideEffect.NavigateBack)
    }

    private fun handleSingleSelect(sectionId: String, optionId: String) = intent {
        val newSelections = state.singleSelections.toMutableMap()
        newSelections[sectionId] = optionId
        reduce {
            state.copy(
                singleSelections = newSelections,
                validationErrors = state.validationErrors - sectionId
            )
        }
    }

    private fun handleMultiSelectToggle(sectionId: String, optionId: String) = intent {
        val currentSet = state.multiSelections[sectionId] ?: emptySet()
        val newSet = if (currentSet.contains(optionId)) {
            currentSet - optionId
        } else {
            currentSet + optionId
        }
        val newSelections = state.multiSelections.toMutableMap()
        newSelections[sectionId] = newSet
        reduce {
            state.copy(
                multiSelections = newSelections,
                validationErrors = state.validationErrors - sectionId
            )
        }
    }

    private fun handleInputChanged(fieldId: String, value: String) = intent {
        val newValues = state.inputValues.toMutableMap()
        newValues[fieldId] = value
        reduce {
            state.copy(
                inputValues = newValues,
                validationErrors = state.validationErrors - fieldId
            )
        }
    }

    private fun handleBoxCountIncrement() = intent {
        reduce { state.copy(boxCount = state.boxCount + 1) }
    }

    private fun handleBoxCountDecrement() = intent {
        if (state.boxCount > 1) {
            reduce { state.copy(boxCount = state.boxCount - 1) }
        }
    }

    private fun handleAddLotClick() = intent {
        if (state.isSubmitting) return@intent

        val params = ValidateAndBuildBoxRequestUseCase.Params(
            boxType = state.getSingleSelection(ValidateAndBuildBoxRequestUseCase.SECTION_BAG_TYPE),
            categoryId = state.getSingleSelection(ValidateAndBuildBoxRequestUseCase.SECTION_CATEGORIES),
            tagIds = state.getMultiSelection(SECTION_TAGS).toList(),
            pickupRange = state.getSingleSelection(ValidateAndBuildBoxRequestUseCase.SECTION_PICKUP_TIME),
            originalPriceText = state.getInputValue(ValidateAndBuildBoxRequestUseCase.FIELD_PRICE_BEFORE),
            discountedPriceText = state.getInputValue(ValidateAndBuildBoxRequestUseCase.FIELD_PRICE_AFTER),
            description = state.getInputValue(SECTION_DESCRIPTION),
            quantity = state.boxCount
        )

        when (val result = validateAndBuildBoxRequest.execute(params)) {
            is ValidateAndBuildBoxRequestUseCase.Result.ValidationErrors -> {
                reduce { state.copy(validationErrors = result.errors) }
                postSideEffect(AddLotSideEffect.ShowValidationError)
            }
            is ValidateAndBuildBoxRequestUseCase.Result.Success -> {
                reduce { state.copy(isSubmitting = true, validationErrors = emptySet()) }

                venuesRepository.createBox(result.request)
                    .onSuccess { data ->
                        reduce { state.copy(isSubmitting = false) }
                        postSideEffect(
                            AddLotSideEffect.ShowSuccess(
                                message = data.message ?: "",
                                warning = data.warning
                            )
                        )
                        postSideEffect(AddLotSideEffect.NavigateToPlacedLots)
                    }
                    .onError {
                        reduce { state.copy(isSubmitting = false) }
                        postSideEffect(AddLotSideEffect.ShowSubmitError)
                    }
            }
        }
    }
}
