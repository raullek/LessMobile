package az.less.mobile.presentation.merchant.add.addlot.model

import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_gluten24dp
import lessmobile.composeapp.generated.resources.ic_gmo24dp
import lessmobile.composeapp.generated.resources.ic_milk24dp
import lessmobile.composeapp.generated.resources.ic_sugar24dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class AddLotResponseModel(
    val sections: List<FormSection> = emptyList()
) {
    companion object {
        const val SECTION_BAG_TYPE = "bag_type"
        const val SECTION_CATEGORIES = "categories"
        const val SECTION_TAGS = "tags"
        const val SECTION_PICKUP_TIME = "pickup_time"
        const val SECTION_PRICE = "price"
        const val SECTION_DESCRIPTION = "description"
        const val SECTION_BOX_COUNT = "box_count"
        const val FIELD_PRICE_BEFORE = "price_before"
        const val FIELD_PRICE_AFTER = "price_after"
        const val CURRENCY_AZN = "AZN"
    }
}

sealed class FormSection {
    abstract val id: String
    abstract val titleRes: StringResource
    abstract val required: Boolean
}

data class ChipsSection(
    override val id: String,
    override val titleRes: StringResource,
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

data class IconGridSection(
    override val id: String,
    override val titleRes: StringResource,
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

data class TimeRangeSelectorSection(
    override val id: String,
    override val titleRes: StringResource,
    override val required: Boolean,
    val multiSelect: Boolean,
    val predefinedRanges: List<TimeRange>
) : FormSection()

data class TimeRange(
    val id: String,
    val from: String,
    val to: String
) {
    val label: String get() = "$from-$to"
}

data class TwoInputsSection(
    override val id: String,
    override val titleRes: StringResource,
    override val required: Boolean,
    val fields: List<InputField>
) : FormSection()

data class InputField(
    val id: String,
    val labelRes: StringResource,
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

data class TextareaSection(
    override val id: String,
    override val titleRes: StringResource,
    override val required: Boolean,
    val maxLength: Int,
    val placeholderRes: StringResource?,
    val defaultValue: String?
) : FormSection()

data class CounterSection(
    override val id: String,
    override val titleRes: StringResource,
    override val required: Boolean = true
) : FormSection() {
    val minValue: Int = 1
}

fun tagValueToIcon(value: String): DrawableResource? = when (value) {
    "sugar_free" -> Res.drawable.ic_sugar24dp
    "gluten_free" -> Res.drawable.ic_gluten24dp
    "gmo_free" -> Res.drawable.ic_gmo24dp
    "lactose_free" -> Res.drawable.ic_milk24dp
    else -> null
}
