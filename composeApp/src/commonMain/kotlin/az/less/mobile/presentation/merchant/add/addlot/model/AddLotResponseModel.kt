package az.less.mobile.presentation.merchant.add.addlot.model

import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_gluten24dp
import lessmobile.composeapp.generated.resources.ic_gmo24dp
import lessmobile.composeapp.generated.resources.ic_milk24dp
import lessmobile.composeapp.generated.resources.ic_sugar24dp
import org.jetbrains.compose.resources.DrawableResource

/**
 * Response model for Add Lot screen
 * Contains dynamic sections that define the form structure
 */
data class AddLotResponseModel(
    val sections: List<FormSection> = emptyList()
)

/**
 * Base sealed class for form sections
 */
sealed class FormSection {
    abstract val id: String
    abstract val title: String
    abstract val required: Boolean
}

/**
 * Chips section - single or multi select chips
 */
data class ChipsSection(
    override val id: String,
    override val title: String,
    override val required: Boolean,
    val multiSelect: Boolean,
    val options: List<ChipOption>
) : FormSection()

data class ChipOption(
    val id: String,
    val label: String,
    val value: String? = null,
    val imageUrl: String? = null
)

/**
 * Icon grid section - grid of selectable items with icons
 */
data class IconGridSection(
    override val id: String,
    override val title: String,
    override val required: Boolean,
    val multiSelect: Boolean,
    val options: List<IconGridOption>
) : FormSection()

data class IconGridOption(
    val id: String,
    val label: String,
    val icon: DrawableResource? = null,
    val imageUrl: String? = null
)

/**
 * Time range selector section
 */
data class TimeRangeSelectorSection(
    override val id: String,
    override val title: String,
    override val required: Boolean,
    val multiSelect: Boolean,
    val predefinedRanges: List<TimeRange>,
    val allowCustom: Boolean
) : FormSection()

data class TimeRange(
    val id: String,
    val from: String,
    val to: String
) {
    val label: String get() = "$from-$to"
}

/**
 * Two inputs section - for price fields etc.
 */
data class TwoInputsSection(
    override val id: String,
    override val title: String,
    override val required: Boolean,
    val fields: List<InputField>
) : FormSection()

data class InputField(
    val id: String,
    val label: String,
    val inputType: InputType,
    val currency: String? = null,
    val validation: InputValidation? = null
)

enum class InputType {
    TEXT, NUMBER, CURRENCY
}

data class InputValidation(
    val min: Double? = null,
    val max: Double? = null
)

/**
 * Textarea section
 */
data class TextareaSection(
    override val id: String,
    override val title: String,
    override val required: Boolean,
    val maxLength: Int,
    val placeholder: String?,
    val defaultValue: String?
) : FormSection()

/**
 * Counter section - for box count
 * minValue is always 0, not configurable from backend
 */
data class CounterSection(
    override val id: String,
    override val title: String,
    override val required: Boolean = true
) : FormSection() {
    val minValue: Int = 0
}

fun tagValueToIcon(value: String): DrawableResource? = when (value) {
    "sugar_free" -> Res.drawable.ic_sugar24dp
    "gluten_free" -> Res.drawable.ic_gluten24dp
    "gmo_free" -> Res.drawable.ic_gmo24dp
    "lactose_free" -> Res.drawable.ic_milk24dp
    else -> null
}
