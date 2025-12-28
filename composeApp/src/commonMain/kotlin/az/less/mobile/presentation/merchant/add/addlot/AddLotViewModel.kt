package az.less.mobile.presentation.merchant.add.addlot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel
import az.less.mobile.presentation.merchant.add.addlot.model.ChipOption
import az.less.mobile.presentation.merchant.add.addlot.model.ChipsSection
import az.less.mobile.presentation.merchant.add.addlot.model.CounterSection
import az.less.mobile.presentation.merchant.add.addlot.model.IconGridOption
import az.less.mobile.presentation.merchant.add.addlot.model.IconGridSection
import az.less.mobile.presentation.merchant.add.addlot.model.InputField
import az.less.mobile.presentation.merchant.add.addlot.model.InputType
import az.less.mobile.presentation.merchant.add.addlot.model.InputValidation
import az.less.mobile.presentation.merchant.add.addlot.model.TextareaSection
import az.less.mobile.presentation.merchant.add.addlot.model.TimeRange
import az.less.mobile.presentation.merchant.add.addlot.model.TimeRangeSelectorSection
import az.less.mobile.presentation.merchant.add.addlot.model.TwoInputsSection
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_gluten24dp
import lessmobile.composeapp.generated.resources.ic_gmo24dp
import lessmobile.composeapp.generated.resources.ic_milk24dp
import lessmobile.composeapp.generated.resources.ic_sugar24dp
import lessmobile.composeapp.generated.resources.test_offer_category_burger
import lessmobile.composeapp.generated.resources.test_offer_category_pasta
import lessmobile.composeapp.generated.resources.test_offer_category_pizza
import lessmobile.composeapp.generated.resources.test_offer_category_sushi
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Add Lot Screen using Orbit MVI
 */
class AddLotViewModel : ViewModel(), ContainerHost<AddLotState, AddLotSideEffect> {

    override val container: Container<AddLotState, AddLotSideEffect> =
        viewModelScope.container(AddLotState())

    init {
        loadData()
    }

    private fun loadData() = intent {
        reduce { state.copy(isLoading = true) }

        // Mock data matching the JSON structure
        val mockResponseModel = AddLotResponseModel(
            sections = listOf(
                // Box type section
                ChipsSection(
                    id = "box_type",
                    title = "Box type",
                    required = true,
                    multiSelect = false,
                    options = listOf(
                        ChipOption(id = "small", label = "Small bag"),
                        ChipOption(id = "medium", label = "Medium bag"),
                        ChipOption(id = "family", label = "Family bag")
                    )
                ),
                // Categories section
                IconGridSection(
                    id = "categories",
                    title = "Choose category",
                    required = true,
                    multiSelect = true,
                    options = listOf(
                        IconGridOption(id = "meals", label = "Meals", icon = Res.drawable.test_offer_category_burger),
                        IconGridOption(id = "lunch", label = "Lunch", icon = Res.drawable.test_offer_category_pasta),
                        IconGridOption(id = "dinner", label = "Dinner", icon = Res.drawable.test_offer_category_pizza),
                        IconGridOption(id = "bakery", label = "Bakery", icon = Res.drawable.test_offer_category_sushi),
                        IconGridOption(id = "dessert", label = "Dessert", icon = Res.drawable.test_offer_category_burger),
                        IconGridOption(id = "grocery", label = "Grocery", icon = Res.drawable.test_offer_category_pasta),
                        IconGridOption(id = "healthy", label = "Healthy", icon = Res.drawable.test_offer_category_pizza)
                    )
                ),
                // Diet tags section
                ChipsSection(
                    id = "diet_tags",
                    title = "Some specific tags (optional)",
                    required = false,
                    multiSelect = true,
                    options = listOf(
                        ChipOption(id = "lactose_free", label = "Lactose free", icon = Res.drawable.ic_milk24dp),
                        ChipOption(id = "sugar_free", label = "Sugar free", icon = Res.drawable.ic_sugar24dp),
                        ChipOption(id = "gluten_free", label = "Gluten free", icon = Res.drawable.ic_gluten24dp),
                        ChipOption(id = "gmo_free", label = "GMO free", icon = Res.drawable.ic_gmo24dp)
                    )
                ),
                // Time slots section
                TimeRangeSelectorSection(
                    id = "time_slots",
                    title = "Choose time range",
                    required = true,
                    multiSelect = true,
                    predefinedRanges = listOf(
                        TimeRange(id = "morning", from = "09:00", to = "12:00"),
                        TimeRange(id = "midday", from = "12:00", to = "15:00"),
                        TimeRange(id = "evening", from = "15:00", to = "18:00")
                    ),
                    allowCustom = true
                ),
                // Price section
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
                ),
                // Description section
                TextareaSection(
                    id = "description",
                    title = "Some info for customer (optional)",
                    required = false,
                    maxLength = 300,
                    placeholder = "Input lot information",
                    defaultValue = null
                ),
                // Box count section
                CounterSection(
                    id = "box_count",
                    title = "Box Count"
                )
            )
        )

        reduce {
            state.copy(
                responseModel = mockResponseModel,
                isLoading = false,
                boxCount = 1
            )
        }
    }

    /**
     * Handle user intents
     */
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
        // Validate required sections
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
                    // Counter always has a value (boxCount in state)
                }
            }
        }

        // TODO: Submit to API
        postSideEffect(AddLotSideEffect.ShowSuccess("Lot added successfully"))
        postSideEffect(AddLotSideEffect.NavigateBack)
    }
}
