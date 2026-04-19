package az.less.mobile.presentation.merchant.add.addlot.model

import az.less.mobile.data.remote.model.FilterOptionsDto
import az.less.mobile.data.remote.model.SelectionType
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.FIELD_PRICE_AFTER
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.FIELD_PRICE_BEFORE
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_BAG_TYPE
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_BOX_COUNT
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_CATEGORIES
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_DESCRIPTION
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_PICKUP_TIME
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_PRICE
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.CURRENCY_AZN
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.SECTION_TAGS
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.add_lot_box_count
import lessmobile.composeapp.generated.resources.add_lot_box_price
import lessmobile.composeapp.generated.resources.add_lot_box_type
import lessmobile.composeapp.generated.resources.add_lot_choose_category
import lessmobile.composeapp.generated.resources.add_lot_choose_time_range
import lessmobile.composeapp.generated.resources.add_lot_description
import lessmobile.composeapp.generated.resources.add_lot_description_placeholder
import lessmobile.composeapp.generated.resources.add_lot_price_after
import lessmobile.composeapp.generated.resources.add_lot_price_before
import lessmobile.composeapp.generated.resources.add_lot_tags

fun FilterOptionsDto.toAddLotResponseModel(): AddLotResponseModel {
    val sections = buildList<FormSection> {
        mapBagTypesSection()?.let { add(it) }
        mapCategoriesSection()?.let { add(it) }
        mapTagsSection()?.let { add(it) }
        mapPickupRangesSection()?.let { add(it) }
        add(createPriceSection())
        add(createDescriptionSection())
        add(createBoxCountSection())
    }
    return AddLotResponseModel(sections = sections)
}

private fun FilterOptionsDto.mapBagTypesSection(): ChipsSection? {
    val section = bagTypes ?: return null
    val options = section.options
        ?.sortedBy { it.sortOrder ?: 0 }
        ?.mapNotNull { bagType ->
            ChipOption(
                id = bagType.id ?: return@mapNotNull null,
                label = bagType.title ?: return@mapNotNull null
            )
        }
        ?.ifEmpty { null }
        ?: return null

    return ChipsSection(
        id = SECTION_BAG_TYPE,
        titleRes = Res.string.add_lot_box_type,
        required = true,
        multiSelect = section.selection == SelectionType.MULTI,
        options = options
    )
}

private fun FilterOptionsDto.mapCategoriesSection(): IconGridSection? {
    val section = categories ?: return null
    val options = section.options
        ?.sortedBy { it.sortOrder ?: 0 }
        ?.mapNotNull { category ->
            IconGridOption(
                id = category.id ?: return@mapNotNull null,
                label = category.title ?: return@mapNotNull null,
                imageUrl = category.imageUrl
            )
        }
        ?.ifEmpty { null }
        ?: return null

    return IconGridSection(
        id = SECTION_CATEGORIES,
        titleRes = Res.string.add_lot_choose_category,
        required = true,
        multiSelect = false, // POST /api/v1/boxes expects single categoryId
        options = options
    )
}

private fun FilterOptionsDto.mapTagsSection(): ChipsSection? {
    val section = tags ?: return null
    val options = section.options
        ?.sortedBy { it.sortOrder ?: 0 }
        ?.mapNotNull { tag ->
            ChipOption(
                id = tag.id ?: return@mapNotNull null,
                label = tag.title ?: return@mapNotNull null,
                value = tag.value,
                imageUrl = tag.imageUrl
            )
        }
        ?.ifEmpty { null }
        ?: return null

    return ChipsSection(
        id = SECTION_TAGS,
        titleRes = Res.string.add_lot_tags,
        required = false,
        multiSelect = section.selection == SelectionType.MULTI,
        options = options
    )
}

private fun FilterOptionsDto.mapPickupRangesSection(): TimeRangeSelectorSection? {
    val section = pickupRanges ?: return null
    val ranges = section.options
        ?.sortedBy { it.sortOrder ?: 0 }
        ?.mapNotNull { range ->
            val id = range.id ?: return@mapNotNull null
            val value = range.value ?: return@mapNotNull null
            val parts = value.split("-")
            TimeRange(
                id = id,
                from = parts.getOrElse(0) { value },
                to = parts.getOrElse(1) { "" }
            )
        }
        ?.ifEmpty { null }
        ?: return null

    return TimeRangeSelectorSection(
        id = SECTION_PICKUP_TIME,
        titleRes = Res.string.add_lot_choose_time_range,
        required = true,
        multiSelect = section.selection == SelectionType.MULTI,
        predefinedRanges = ranges
    )
}

private fun createPriceSection() = TwoInputsSection(
    id = SECTION_PRICE,
    titleRes = Res.string.add_lot_box_price,
    required = true,
    fields = listOf(
        InputField(
            id = FIELD_PRICE_BEFORE,
            labelRes = Res.string.add_lot_price_before,
            inputType = InputType.CURRENCY,
            currency = CURRENCY_AZN,
            validation = InputValidation(min = 0.0)
        ),
        InputField(
            id = FIELD_PRICE_AFTER,
            labelRes = Res.string.add_lot_price_after,
            inputType = InputType.CURRENCY,
            currency = CURRENCY_AZN,
            validation = InputValidation(min = 0.0)
        )
    )
)

private fun createDescriptionSection() = TextareaSection(
    id = SECTION_DESCRIPTION,
    titleRes = Res.string.add_lot_description,
    required = false,
    maxLength = 300,
    placeholderRes = Res.string.add_lot_description_placeholder,
    defaultValue = null
)

private fun createBoxCountSection() = CounterSection(
    id = SECTION_BOX_COUNT,
    titleRes = Res.string.add_lot_box_count
)
