package az.less.mobile.presentation.merchant.add.addlot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.DsToolBar
import az.less.mobile.presentation.merchant.add.addlot.components.BoxCountSelector
import az.less.mobile.presentation.merchant.add.addlot.components.BoxTypeButton
import az.less.mobile.presentation.merchant.add.addlot.components.CategoryGridItem
import az.less.mobile.presentation.merchant.add.addlot.components.TagChip
import az.less.mobile.presentation.merchant.add.addlot.components.TimeSlotButton
import az.less.mobile.presentation.merchant.add.addlot.model.ChipsSection
import az.less.mobile.presentation.merchant.add.addlot.model.CounterSection
import az.less.mobile.presentation.merchant.add.addlot.model.FormSection
import az.less.mobile.presentation.merchant.add.addlot.model.IconGridSection
import az.less.mobile.presentation.merchant.add.addlot.model.InputType
import az.less.mobile.presentation.merchant.add.addlot.model.TextareaSection
import az.less.mobile.presentation.merchant.add.addlot.model.TimeRangeSelectorSection
import az.less.mobile.presentation.merchant.add.addlot.model.TwoInputsSection
import az.less.mobile.presentation.merchant.add.addlot.model.tagValueToIcon
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.add_lot_button
import lessmobile.composeapp.generated.resources.add_lot_loading
import lessmobile.composeapp.generated.resources.add_lot_select_time
import lessmobile.composeapp.generated.resources.add_lot_title
import lessmobile.composeapp.generated.resources.error_generic
import lessmobile.composeapp.generated.resources.error_generic_subtitle
import lessmobile.composeapp.generated.resources.ic_info_24dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful AddLotScreen that connects to ViewModel
 */
@Composable
fun AddLotScreen(
    viewModel: AddLotViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AddLotSideEffect.NavigateBack -> navController.popBackStack()
            is AddLotSideEffect.NavigateToNext -> {}
            is AddLotSideEffect.ShowError -> {}
            is AddLotSideEffect.ShowSuccess -> {}
        }
    }

    AddLotScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless AddLotScreen UI implementation
 */
@Composable
fun AddLotScreenContent(
    state: AddLotState,
    onIntent: (AddLotIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundPrimary)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        DsToolBar(
            title = stringResource(Res.string.add_lot_title),
            onBackClick = { onIntent(AddLotIntent.OnBackClick) },
            backgroundColor = LessTheme.colors.backgroundPrimary
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LessTheme.colors.elementsPrimaryBrand)
                }
            }
            state.hasError -> {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LessTheme.spacing.large),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_info_24dp),
                            contentDescription = null,
                            tint = LessTheme.colors.textIconsGrey,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(LessTheme.spacing.large))

                        Text(
                            text = stringResource(Res.string.error_generic),
                            style = LessTheme.typography.title24Bold,
                            color = LessTheme.colors.textIconsBlack,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

                        Text(
                            text = stringResource(Res.string.error_generic_subtitle),
                            style = LessTheme.typography.body16Regular,
                            color = LessTheme.colors.textIconsGrey,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            state.isEmpty -> {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.add_lot_loading),
                        style = LessTheme.typography.body16Regular,
                        color = LessTheme.colors.textIconsGrey
                    )
                }
            }
            else -> {
                val sections = state.responseModel?.sections ?: emptyList()

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = LessTheme.spacing.medium,
                        end = LessTheme.spacing.medium,
                        top = LessTheme.spacing.medium,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium)
                ) {
                    items(sections, key = { it.id }) { section ->
                        SectionRenderer(
                            section = section,
                            state = state,
                            onIntent = onIntent
                        )
                    }

                    // Add Lots Button (local, not from backend)
                    item(key = "addButton") {
                        DsButton(
                            text = stringResource(Res.string.add_lot_button),
                            onClick = { onIntent(AddLotIntent.OnAddLotClick) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = LessTheme.spacing.medium),
                            variant = ButtonVariant.Primary,
                            size = ButtonSize.Large
                        )
                    }
                }
            }
        }
    }

    if (state.showCustomTimePicker) {
        // TODO: Show time picker bottom sheet
    }
}

@Composable
private fun SectionRenderer(
    section: FormSection,
    state: AddLotState,
    onIntent: (AddLotIntent) -> Unit
) {
    when (section) {
        is ChipsSection -> ChipsSectionContent(section, state, onIntent)
        is IconGridSection -> IconGridSectionContent(section, state, onIntent)
        is TimeRangeSelectorSection -> TimeRangeSectionContent(section, state, onIntent)
        is TwoInputsSection -> TwoInputsSectionContent(section, state, onIntent)
        is TextareaSection -> TextareaSectionContent(section, state, onIntent)
        is CounterSection -> CounterSectionContent(section, state, onIntent)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipsSectionContent(
    section: ChipsSection,
    state: AddLotState,
    onIntent: (AddLotIntent) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = LessTheme.spacing.small)) {
        Text(
            text = section.title,
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary,
            modifier = Modifier.padding(bottom = LessTheme.spacing.small)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall),
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
        ) {
            section.options.forEach { option ->
                val isSelected = if (section.multiSelect) {
                    state.getMultiSelection(section.id).contains(option.id)
                } else {
                    state.getSingleSelection(section.id) == option.id
                }

                val hasIcon = option.imageUrl != null || option.value?.let { tagValueToIcon(it) } != null
                if (hasIcon) {
                    TagChip(
                        label = option.label,
                        isSelected = isSelected,
                        imageUrl = option.imageUrl,
                        icon = option.value?.let { tagValueToIcon(it) },
                        onToggle = {
                            if (section.multiSelect) {
                                onIntent(AddLotIntent.OnMultiSelectToggle(section.id, option.id))
                            } else {
                                onIntent(AddLotIntent.OnSingleSelect(section.id, option.id))
                            }
                        }
                    )
                } else {
                    BoxTypeButton(
                        text = option.label,
                        isSelected = isSelected,
                        onClick = {
                            if (section.multiSelect) {
                                onIntent(AddLotIntent.OnMultiSelectToggle(section.id, option.id))
                            } else {
                                onIntent(AddLotIntent.OnSingleSelect(section.id, option.id))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun IconGridSectionContent(
    section: IconGridSection,
    state: AddLotState,
    onIntent: (AddLotIntent) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = LessTheme.spacing.small)) {
        Text(
            text = section.title,
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary,
            modifier = Modifier.padding(bottom = LessTheme.spacing.small)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
        ) {
            val firstRowOptions = section.options.take(4)
            val secondRowOptions = section.options.drop(4)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
            ) {
                firstRowOptions.forEach { option ->
                    val isSelected = state.getMultiSelection(section.id).contains(option.id)
                    CategoryGridItem(
                        option = option,
                        isSelected = isSelected,
                        onSelected = {
                            onIntent(AddLotIntent.OnMultiSelectToggle(section.id, option.id))
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(4 - firstRowOptions.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            if (secondRowOptions.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
                ) {
                    secondRowOptions.forEach { option ->
                        val isSelected = state.getMultiSelection(section.id).contains(option.id)
                        CategoryGridItem(
                            option = option,
                            isSelected = isSelected,
                            onSelected = {
                                onIntent(AddLotIntent.OnMultiSelectToggle(section.id, option.id))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(4 - secondRowOptions.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimeRangeSectionContent(
    section: TimeRangeSelectorSection,
    state: AddLotState,
    onIntent: (AddLotIntent) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = LessTheme.spacing.small)) {
        Text(
            text = section.title,
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary,
            modifier = Modifier.padding(bottom = LessTheme.spacing.small)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall),
            verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xSmall)
        ) {
            section.predefinedRanges.forEach { timeRange ->
                val isSelected = state.getMultiSelection(section.id).contains(timeRange.id)
                TimeSlotButton(
                    label = timeRange.label,
                    isSelected = isSelected,
                    onClick = {
                        onIntent(AddLotIntent.OnMultiSelectToggle(section.id, timeRange.id))
                    }
                )
            }

            if (section.allowCustom) {
                val selectTimeLabel = stringResource(Res.string.add_lot_select_time)
                val customLabel = if (state.customTimeStart != null && state.customTimeEnd != null) {
                    "${state.customTimeStart}-${state.customTimeEnd}"
                } else {
                    selectTimeLabel
                }
                val isCustomSelected = state.customTimeStart != null && state.customTimeEnd != null
                TimeSlotButton(
                    label = customLabel,
                    isSelected = isCustomSelected,
                    onClick = { onIntent(AddLotIntent.OnCustomTimeClick(section.id)) }
                )
            }
        }
    }
}

@Composable
private fun TwoInputsSectionContent(
    section: TwoInputsSection,
    state: AddLotState,
    onIntent: (AddLotIntent) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = LessTheme.spacing.small)) {
        Text(
            text = section.title,
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary,
            modifier = Modifier.padding(bottom = LessTheme.spacing.small)
        )

        section.fields.forEachIndexed { index, field ->
            val keyboardType = when (field.inputType) {
                InputType.CURRENCY, InputType.NUMBER -> KeyboardType.Number
                InputType.TEXT -> KeyboardType.Text
            }

            DsTextField(
                value = state.getInputValue(field.id),
                onValueChange = { newValue ->
                    if (field.inputType == InputType.CURRENCY || field.inputType == InputType.NUMBER) {
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            onIntent(AddLotIntent.OnInputChanged(field.id, newValue))
                        }
                    } else {
                        onIntent(AddLotIntent.OnInputChanged(field.id, newValue))
                    }
                },
                placeholder = field.label,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                onEndIconClick = { onIntent(AddLotIntent.OnInputChanged(field.id, "")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (index < section.fields.size - 1) {
                            Modifier.padding(bottom = LessTheme.spacing.medium)
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}

@Composable
private fun TextareaSectionContent(
    section: TextareaSection,
    state: AddLotState,
    onIntent: (AddLotIntent) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = LessTheme.spacing.small)) {
        Text(
            text = section.title,
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary,
            modifier = Modifier.padding(bottom = LessTheme.spacing.small)
        )
        DsTextField(
            value = state.getInputValue(section.id),
            onValueChange = { newValue ->
                val trimmed = if (newValue.length > section.maxLength) {
                    newValue.take(section.maxLength)
                } else {
                    newValue
                }
                onIntent(AddLotIntent.OnInputChanged(section.id, trimmed))
            },
            placeholder = section.placeholder ?: "",
            singleLine = false,
            maxLines = 5,
            onEndIconClick = { onIntent(AddLotIntent.OnInputChanged(section.id, "")) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CounterSectionContent(
    section: CounterSection,
    state: AddLotState,
    onIntent: (AddLotIntent) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = LessTheme.spacing.small)) {
        Text(
            text = section.title,
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary,
            modifier = Modifier.padding(bottom = LessTheme.spacing.small)
        )
        BoxCountSelector(
            count = state.boxCount,
            minValue = section.minValue,
            onIncrement = { onIntent(AddLotIntent.OnBoxCountIncrement) },
            onDecrement = { onIntent(AddLotIntent.OnBoxCountDecrement) }
        )
    }
}
