package az.less.mobile.presentation.merchant.add.addlot

import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel

/**
 * State of the Add Lot Screen
 * Uses dynamic maps to store selections based on section IDs
 */
data class AddLotState(
    val isLoading: Boolean = false,
    val responseModel: AddLotResponseModel? = null,

    // Dynamic selections by section ID
    // For single select: Map<sectionId, selectedOptionId>
    val singleSelections: Map<String, String> = emptyMap(),
    // For multi select: Map<sectionId, Set<selectedOptionIds>>
    val multiSelections: Map<String, Set<String>> = emptyMap(),
    // For input fields: Map<fieldId, value>
    val inputValues: Map<String, String> = emptyMap(),

    // Local fields (not from backend)
    val boxCount: Int = 1,

    // Custom time picker state
    val customTimeStart: String? = null,
    val customTimeEnd: String? = null,
    val showCustomTimePicker: Boolean = false,
    val timePickerType: TimePickerType? = null,
    val activeTimeSection: String? = null
) {
    val isEmpty: Boolean get() = responseModel == null && !isLoading

    // Helper functions to get values
    fun getSingleSelection(sectionId: String): String? = singleSelections[sectionId]
    fun getMultiSelection(sectionId: String): Set<String> = multiSelections[sectionId] ?: emptySet()
    fun getInputValue(fieldId: String): String = inputValues[fieldId] ?: ""
}

enum class TimePickerType {
    START_TIME,
    END_TIME
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface AddLotSideEffect {
    data object NavigateBack : AddLotSideEffect
    data object NavigateToNext : AddLotSideEffect
    data class ShowError(val message: String) : AddLotSideEffect
    data class ShowSuccess(val message: String) : AddLotSideEffect
}

/**
 * User Intents/Actions - Generic for dynamic sections
 */
sealed interface AddLotIntent {
    data object OnBackClick : AddLotIntent
    data object OnLoadData : AddLotIntent

    // Single selection (chips, time slots)
    data class OnSingleSelect(val sectionId: String, val optionId: String) : AddLotIntent

    // Multi selection (tags, categories)
    data class OnMultiSelectToggle(val sectionId: String, val optionId: String) : AddLotIntent

    // Input fields
    data class OnInputChanged(val fieldId: String, val value: String) : AddLotIntent

    // Box count (local, not from backend)
    data object OnBoxCountIncrement : AddLotIntent
    data object OnBoxCountDecrement : AddLotIntent

    // Custom time picker
    data class OnCustomTimeClick(val sectionId: String) : AddLotIntent
    data class OnTimeSelected(val time: String, val type: TimePickerType) : AddLotIntent
    data object OnDismissTimePicker : AddLotIntent

    // Submit
    data object OnAddLotClick : AddLotIntent
}
