package az.less.mobile.presentation.merchant.add.addlot

import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel

/**
 * State of the Add Lot Screen
 * Uses dynamic maps to store selections based on section IDs
 */
data class AddLotState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
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

    // Validation errors - set of section/field IDs that failed validation
    val validationErrors: Set<String> = emptySet()
) {
    val isEmpty: Boolean get() = responseModel == null && !isLoading && error == null
    val hasError: Boolean get() = error != null

    // Helper functions to get values
    fun getSingleSelection(sectionId: String): String? = singleSelections[sectionId]
    fun getMultiSelection(sectionId: String): Set<String> = multiSelections[sectionId] ?: emptySet()
    fun getInputValue(fieldId: String): String = inputValues[fieldId] ?: ""
    fun hasValidationError(sectionId: String): Boolean = validationErrors.contains(sectionId)
}

/**
 * Side Effects for navigation and one-time events
 */
sealed interface AddLotSideEffect {
    data object NavigateBack : AddLotSideEffect
    data object NavigateToPlacedLots : AddLotSideEffect
    data object ShowValidationError : AddLotSideEffect
    data object ShowSubmitError : AddLotSideEffect
    data class ShowSuccess(val message: String, val warning: String?) : AddLotSideEffect
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

    // Submit
    data object OnAddLotClick : AddLotIntent
}
