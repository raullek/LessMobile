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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField
import az.less.designsystem.components.AnimatedToast
import az.less.designsystem.components.DsToolBar
import az.less.designsystem.components.ToastType
import kotlinx.coroutines.delay
import az.less.mobile.presentation.merchant.add.addlot.components.BoxCountSelector
import az.less.mobile.presentation.merchant.add.addlot.components.BoxTypeButton
import az.less.mobile.presentation.merchant.add.addlot.components.CategoryGridItem
import az.less.mobile.presentation.merchant.add.addlot.components.TagChip
import az.less.mobile.presentation.merchant.add.addlot.components.TimeSlotButton
import az.less.mobile.presentation.merchant.orders.MERCH_ORDERS_SELECT_TAB_KEY
import az.less.mobile.presentation.merchant.orders.model.MerchOrderTab
import az.less.mobile.presentation.merchant.add.addlot.model.ChipsSection
import az.less.mobile.presentation.merchant.add.addlot.model.CounterSection
import az.less.mobile.presentation.merchant.add.addlot.model.FormSection
import az.less.mobile.presentation.merchant.add.addlot.model.IconGridSection
import az.less.mobile.presentation.merchant.add.addlot.model.InputType
import az.less.mobile.presentation.merchant.add.addlot.model.TextareaSection
import az.less.mobile.presentation.merchant.add.addlot.model.TimeRangeSelectorSection
import az.less.mobile.presentation.merchant.add.addlot.model.TwoInputsSection
import az.less.mobile.presentation.merchant.add.addlot.model.AddLotResponseModel.Companion.FIELD_PRICE_BEFORE
import az.less.mobile.presentation.merchant.add.addlot.model.tagValueToIcon
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.add_lot_button
import lessmobile.composeapp.generated.resources.add_lot_error_submit
import lessmobile.composeapp.generated.resources.add_lot_error_validation
import lessmobile.composeapp.generated.resources.add_lot_success
import lessmobile.composeapp.generated.resources.add_lot_loading
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
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.Success) }

    val validationErrorMsg = stringResource(Res.string.add_lot_error_validation)
    val submitErrorMsg = stringResource(Res.string.add_lot_error_submit)
    val fallbackSuccessMsg = stringResource(Res.string.add_lot_success)

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2000)
            toastMessage = null
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AddLotSideEffect.NavigateBack -> navController.popBackStack()
            is AddLotSideEffect.NavigateToPlacedLots -> {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(MERCH_ORDERS_SELECT_TAB_KEY, MerchOrderTab.AWAITING_PURCHASE.name)
                navController.popBackStack()
            }
            is AddLotSideEffect.ShowValidationError -> {
                toastType = ToastType.Error
                toastMessage = validationErrorMsg
            }
            is AddLotSideEffect.ShowSubmitError -> {
                toastType = ToastType.Error
                toastMessage = submitErrorMsg
            }
            is AddLotSideEffect.ShowSuccess -> {
                toastType = ToastType.Success
                val msg = sideEffect.message.ifEmpty { fallbackSuccessMsg }
                toastMessage = if (sideEffect.warning != null) {
                    "$msg\n${sideEffect.warning}"
                } else {
                    msg
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AddLotScreenContent(
            state = state,
            onIntent = viewModel::onIntent
        )

        AnimatedToast(
            visible = toastMessage != null,
            title = toastMessage ?: "",
            type = toastType,
            modifier = Modifier.align(Alignment.TopCenter),
            showGradientScrim = false
        )
    }
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
                    // Dynamic sections from backend
                    items(sections, key = { it.id }) { section ->
                        SectionRenderer(
                            section = section,
                            state = state,
                            onIntent = onIntent
                        )
                    }

                    // Add Lots Button
                    item(key = "addButton") {
                        DsButton(
                            text = stringResource(Res.string.add_lot_button),
                            onClick = { onIntent(AddLotIntent.OnAddLotClick) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = LessTheme.spacing.medium),
                            variant = ButtonVariant.Primary,
                            size = ButtonSize.Large,
                            isLoading = state.isSubmitting
                        )
                    }
                }
            }
        }
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
            text = stringResource(section.titleRes),
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
                val sectionHasError = state.hasValidationError(section.id)
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
                        hasError = sectionHasError,
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
            text = stringResource(section.titleRes),
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary,
            modifier = Modifier.padding(bottom = LessTheme.spacing.small)
        )

        val sectionHasError = state.hasValidationError(section.id)

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
                    val isSelected = if (section.multiSelect) {
                        state.getMultiSelection(section.id).contains(option.id)
                    } else {
                        state.getSingleSelection(section.id) == option.id
                    }
                    CategoryGridItem(
                        option = option,
                        isSelected = isSelected,
                        hasError = sectionHasError,
                        onSelected = {
                            if (section.multiSelect) {
                                onIntent(AddLotIntent.OnMultiSelectToggle(section.id, option.id))
                            } else {
                                onIntent(AddLotIntent.OnSingleSelect(section.id, option.id))
                            }
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
                        val isSelected = if (section.multiSelect) {
                            state.getMultiSelection(section.id).contains(option.id)
                        } else {
                            state.getSingleSelection(section.id) == option.id
                        }
                        CategoryGridItem(
                            option = option,
                            isSelected = isSelected,
                            hasError = sectionHasError,
                            onSelected = {
                                if (section.multiSelect) {
                                    onIntent(AddLotIntent.OnMultiSelectToggle(section.id, option.id))
                                } else {
                                    onIntent(AddLotIntent.OnSingleSelect(section.id, option.id))
                                }
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
            text = stringResource(section.titleRes),
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
                val isSelected = if (section.multiSelect) {
                    state.getMultiSelection(section.id).contains(timeRange.id)
                } else {
                    state.getSingleSelection(section.id) == timeRange.id
                }
                TimeSlotButton(
                    label = timeRange.label,
                    isSelected = isSelected,
                    hasError = state.hasValidationError(section.id),
                    onClick = {
                        if (section.multiSelect) {
                            onIntent(AddLotIntent.OnMultiSelectToggle(section.id, timeRange.id))
                        } else {
                            onIntent(AddLotIntent.OnSingleSelect(section.id, timeRange.id))
                        }
                    }
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
            text = stringResource(section.titleRes),
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsSecondary,
            modifier = Modifier.padding(bottom = LessTheme.spacing.small)
        )

        section.fields.forEachIndexed { index, field ->
            val keyboardType = when (field.inputType) {
                InputType.CURRENCY, InputType.NUMBER -> KeyboardType.Number
                InputType.TEXT -> KeyboardType.Text
            }

            val visualTransformation = if (field.id == FIELD_PRICE_BEFORE) {
                StrikethroughTransformation
            } else {
                VisualTransformation.None
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
                placeholder = stringResource(field.labelRes),
                isError = state.hasValidationError(field.id),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = visualTransformation,
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
            text = stringResource(section.titleRes),
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
            placeholder = section.placeholderRes?.let { stringResource(it) } ?: "",
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
            text = stringResource(section.titleRes),
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

private object StrikethroughTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val styled = AnnotatedString(
            text = text.text,
            spanStyles = text.spanStyles + listOf(
                AnnotatedString.Range(
                    item = androidx.compose.ui.text.SpanStyle(
                        textDecoration = TextDecoration.LineThrough
                    ),
                    start = 0,
                    end = text.length
                )
            )
        )
        return TransformedText(styled, OffsetMapping.Identity)
    }
}
