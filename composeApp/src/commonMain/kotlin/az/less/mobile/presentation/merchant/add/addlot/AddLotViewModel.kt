package az.less.mobile.presentation.merchant.add.addlot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.ContentRepository
import az.less.mobile.presentation.merchant.add.addlot.model.ChipsSection
import az.less.mobile.presentation.merchant.add.addlot.model.CounterSection
import az.less.mobile.presentation.merchant.add.addlot.model.IconGridSection
import az.less.mobile.presentation.merchant.add.addlot.model.TextareaSection
import az.less.mobile.presentation.merchant.add.addlot.model.TimeRangeSelectorSection
import az.less.mobile.presentation.merchant.add.addlot.model.TwoInputsSection
import az.less.mobile.presentation.merchant.add.addlot.model.toAddLotResponseModel
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.add_lot_error_enter
import lessmobile.composeapp.generated.resources.add_lot_error_select
import lessmobile.composeapp.generated.resources.add_lot_success
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class AddLotViewModel(
    private val contentRepository: ContentRepository
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
            is AddLotIntent.OnCustomTimeClick -> handleCustomTimeClick(intent.sectionId)
            is AddLotIntent.OnTimeSelected -> handleTimeSelected(intent.time, intent.type)
            is AddLotIntent.OnDismissTimePicker -> handleDismissTimePicker()
            is AddLotIntent.OnAddLotClick -> handleAddLotClick()
        }
    }

    private fun handleBackClick() = intent {
        postSideEffect(AddLotSideEffect.NavigateBack)
    }

    private fun handleSingleSelect(sectionId: String, optionId: String) = intent {
        val newSelections = state.singleSelections.toMutableMap()
        newSelections[sectionId] = optionId
        reduce { state.copy(singleSelections = newSelections) }
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
        reduce { state.copy(multiSelections = newSelections) }
    }

    private fun handleInputChanged(fieldId: String, value: String) = intent {
        val newValues = state.inputValues.toMutableMap()
        newValues[fieldId] = value
        reduce { state.copy(inputValues = newValues) }
    }

    private fun handleBoxCountIncrement() = intent {
        reduce { state.copy(boxCount = state.boxCount + 1) }
    }

    private fun handleBoxCountDecrement() = intent {
        if (state.boxCount > 0) {
            reduce { state.copy(boxCount = state.boxCount - 1) }
        }
    }

    private fun handleCustomTimeClick(sectionId: String) = intent {
        reduce {
            state.copy(
                showCustomTimePicker = true,
                activeTimeSection = sectionId
            )
        }
    }

    private fun handleTimeSelected(time: String, type: TimePickerType) = intent {
        reduce {
            when (type) {
                TimePickerType.START_TIME -> state.copy(
                    customTimeStart = time,
                    showCustomTimePicker = false,
                    timePickerType = null
                )
                TimePickerType.END_TIME -> state.copy(
                    customTimeEnd = time,
                    showCustomTimePicker = false,
                    timePickerType = null
                )
            }
        }
    }

    private fun handleDismissTimePicker() = intent {
        reduce {
            state.copy(
                showCustomTimePicker = false,
                timePickerType = null
            )
        }
    }

    private fun handleAddLotClick() = intent {
        val sections = state.responseModel?.sections ?: return@intent

        for (section in sections) {
            if (!section.required) continue

            when (section) {
                is ChipsSection -> {
                    val hasSelection = if (section.multiSelect) {
                        state.getMultiSelection(section.id).isNotEmpty()
                    } else {
                        state.getSingleSelection(section.id) != null
                    }
                    if (!hasSelection) {
                        postSideEffect(AddLotSideEffect.ShowError(Res.string.add_lot_error_select, listOf(section.titleRes)))
                        return@intent
                    }
                }
                is IconGridSection -> {
                    val hasSelection = if (section.multiSelect) {
                        state.getMultiSelection(section.id).isNotEmpty()
                    } else {
                        state.getSingleSelection(section.id) != null
                    }
                    if (!hasSelection) {
                        postSideEffect(AddLotSideEffect.ShowError(Res.string.add_lot_error_select, listOf(section.titleRes)))
                        return@intent
                    }
                }
                is TimeRangeSelectorSection -> {
                    val hasSelection = if (section.multiSelect) {
                        state.getMultiSelection(section.id).isNotEmpty()
                    } else {
                        state.getSingleSelection(section.id) != null
                    } || (state.customTimeStart != null && state.customTimeEnd != null)
                    if (!hasSelection) {
                        postSideEffect(AddLotSideEffect.ShowError(Res.string.add_lot_error_select, listOf(section.titleRes)))
                        return@intent
                    }
                }
                is TwoInputsSection -> {
                    for (field in section.fields) {
                        if (state.getInputValue(field.id).isEmpty()) {
                            postSideEffect(AddLotSideEffect.ShowError(Res.string.add_lot_error_enter, listOf(field.labelRes)))
                            return@intent
                        }
                    }
                }
                is TextareaSection -> {
                    if (state.getInputValue(section.id).isEmpty()) {
                        postSideEffect(AddLotSideEffect.ShowError(Res.string.add_lot_error_enter, listOf(section.titleRes)))
                        return@intent
                    }
                }
                is CounterSection -> {
                    // Counter always has a value
                }
            }
        }

        // TODO: Submit to API
        postSideEffect(AddLotSideEffect.ShowSuccess(Res.string.add_lot_success))
        postSideEffect(AddLotSideEffect.NavigateBack)
    }
}
