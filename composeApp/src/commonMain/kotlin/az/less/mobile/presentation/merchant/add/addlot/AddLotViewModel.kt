package az.less.mobile.presentation.merchant.add.addlot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.data.remote.model.FilterOptionsDto
import az.less.mobile.domain.repository.ContentRepository
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel
import az.less.mobile.presentation.merchant.add.addlot.model.ChipOption
import az.less.mobile.presentation.merchant.add.addlot.model.ChipsSection
import az.less.mobile.presentation.merchant.add.addlot.model.CounterSection
import az.less.mobile.presentation.merchant.add.addlot.model.IconGridSection
import az.less.mobile.presentation.merchant.add.addlot.model.IconGridOption
import az.less.mobile.presentation.merchant.add.addlot.model.InputField
import az.less.mobile.presentation.merchant.add.addlot.model.InputType
import az.less.mobile.presentation.merchant.add.addlot.model.InputValidation
import az.less.mobile.presentation.merchant.add.addlot.model.TextareaSection
import az.less.mobile.presentation.merchant.add.addlot.model.TimeRange
import az.less.mobile.presentation.merchant.add.addlot.model.TimeRangeSelectorSection
import az.less.mobile.presentation.merchant.add.addlot.model.TwoInputsSection
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
                val responseModel = mapFilterOptionsToFormSections(data)
                reduce {
                    state.copy(
                        responseModel = responseModel,
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

    private fun mapFilterOptionsToFormSections(dto: FilterOptionsDto): AddLotResponseModel {
        val sections = buildList {
            // Bag types → ChipsSection (single select)
            if (dto.bagTypes.isNotEmpty()) {
                add(
                    ChipsSection(
                        id = "bag_type",
                        title = "Box type",
                        required = true,
                        multiSelect = false,
                        options = dto.bagTypes.map { bagType ->
                            ChipOption(
                                id = bagType.value,
                                label = bagType.key
                            )
                        }
                    )
                )
            }

            // Categories → IconGridSection (multi select)
            if (dto.categories.isNotEmpty()) {
                add(
                    IconGridSection(
                        id = "categories",
                        title = "Choose category",
                        required = true,
                        multiSelect = true,
                        options = dto.categories
                            .sortedBy { it.sortOrder }
                            .map { category ->
                                IconGridOption(
                                    id = category.id,
                                    label = category.title,
                                    imageUrl = category.imageUrl
                                )
                            }
                    )
                )
            }

            // Tags → ChipsSection (multi select, optional)
            if (dto.tags.isNotEmpty()) {
                add(
                    ChipsSection(
                        id = "tags",
                        title = "Some specific tags (optional)",
                        required = false,
                        multiSelect = true,
                        options = dto.tags
                            .sortedBy { it.sortOrder }
                            .map { tag ->
                                ChipOption(
                                    id = tag.id,
                                    label = tag.title,
                                    value = tag.value,
                                    imageUrl = tag.imageUrl
                                )
                            }
                    )
                )
            }

            // Pickup ranges → TimeRangeSelectorSection
            if (dto.pickupRanges.isNotEmpty()) {
                add(
                    TimeRangeSelectorSection(
                        id = "pickup_time",
                        title = "Choose time range",
                        required = true,
                        multiSelect = true,
                        predefinedRanges = dto.pickupRanges.mapIndexed { index, range ->
                            val parts = range.value.split("-")
                            TimeRange(
                                id = "range_$index",
                                from = parts.getOrElse(0) { range.value },
                                to = parts.getOrElse(1) { "" }
                            )
                        },
                        allowCustom = true
                    )
                )
            }

            // Static sections (not from API)
            // Box price
            add(
                TwoInputsSection(
                    id = "price",
                    title = "Box price",
                    required = true,
                    fields = listOf(
                        InputField(
                            id = "price_before",
                            label = "Price before",
                            inputType = InputType.CURRENCY,
                            currency = "AZN",
                            validation = InputValidation(min = 0.0)
                        ),
                        InputField(
                            id = "price_after",
                            label = "Price after discount",
                            inputType = InputType.CURRENCY,
                            currency = "AZN",
                            validation = InputValidation(min = 0.0)
                        )
                    )
                )
            )

            // Description
            add(
                TextareaSection(
                    id = "description",
                    title = "Some info for customer (optional)",
                    required = false,
                    maxLength = 300,
                    placeholder = "Input lot information",
                    defaultValue = null
                )
            )

            // Box count
            add(
                CounterSection(
                    id = "box_count",
                    title = "Box Count"
                )
            )
        }

        return AddLotResponseModel(sections = sections)
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
                    if (section.multiSelect) {
                        if (state.getMultiSelection(section.id).isEmpty()) {
                            postSideEffect(AddLotSideEffect.ShowError("Please select ${section.title}"))
                            return@intent
                        }
                    } else {
                        if (state.getSingleSelection(section.id) == null) {
                            postSideEffect(AddLotSideEffect.ShowError("Please select ${section.title}"))
                            return@intent
                        }
                    }
                }
                is IconGridSection -> {
                    if (state.getMultiSelection(section.id).isEmpty()) {
                        postSideEffect(AddLotSideEffect.ShowError("Please select ${section.title}"))
                        return@intent
                    }
                }
                is TimeRangeSelectorSection -> {
                    val hasSelection = state.getMultiSelection(section.id).isNotEmpty() ||
                            (state.customTimeStart != null && state.customTimeEnd != null)
                    if (!hasSelection) {
                        postSideEffect(AddLotSideEffect.ShowError("Please select ${section.title}"))
                        return@intent
                    }
                }
                is TwoInputsSection -> {
                    for (field in section.fields) {
                        if (state.getInputValue(field.id).isEmpty()) {
                            postSideEffect(AddLotSideEffect.ShowError("Please enter ${field.label}"))
                            return@intent
                        }
                    }
                }
                is TextareaSection -> {
                    if (state.getInputValue(section.id).isEmpty()) {
                        postSideEffect(AddLotSideEffect.ShowError("Please enter ${section.title}"))
                        return@intent
                    }
                }
                is CounterSection -> {
                    // Counter always has a value
                }
            }
        }

        // TODO: Submit to API
        postSideEffect(AddLotSideEffect.ShowSuccess("Lot added successfully"))
        postSideEffect(AddLotSideEffect.NavigateBack)
    }
}
