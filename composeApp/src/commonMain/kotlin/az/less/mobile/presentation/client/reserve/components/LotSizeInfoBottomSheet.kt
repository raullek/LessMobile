package az.less.mobile.presentation.client.reserve.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonSize
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.mobile.presentation.client.reserve.LotSizeInfo

/**
 * Bottom sheet showing lot size information
 * Based on Figma design: https://www.figma.com/design/LfrtpXNQmOc01fJRhY6Iwq/Less-App---EDU?node-id=2794-9260&m=dev
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotSizeInfoBottomSheet(
    isVisible: Boolean,
    sheetState: SheetState,
    lotSizeInfoList: List<LotSizeInfo>,
    onDismiss: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = LessTheme.colors.backgroundSecond,
            contentColor = LessTheme.colors.textIconsBlack,
            shape = RoundedCornerShape(topStart = LessTheme.radius.medium, topEnd = LessTheme.radius.medium),
            dragHandle = null,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            LotSizeInfoContent(
                lotSizeInfoList = lotSizeInfoList,
                onGotItClick = onDismiss
            )
        }
    }
}

@Composable
private fun LotSizeInfoContent(
    lotSizeInfoList: List<LotSizeInfo>,
    onGotItClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Drag handle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = LessTheme.spacing.xSmall),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(LessTheme.size.large)
                    .height(3.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(LessTheme.colors.borderPrimary)
            )
        }

        // Lot size info list
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = LessTheme.spacing.large)
        ) {
            lotSizeInfoList.forEachIndexed { index, item ->
                LotSizeInfoItem(
                    name = item.name,
                    description = item.description,
                    showDivider = index < lotSizeInfoList.size - 1
                )
            }
        }

        // Got it button
        DsButton(
            text = "Got it",
            onClick = onGotItClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium)
                .padding(bottom = LessTheme.spacing.medium),
            variant = ButtonVariant.Primary,
            size = ButtonSize.Large
        )

        // Bottom spacing for home indicator
        Spacer(modifier = Modifier.height(LessTheme.size.xLarge))
    }
}

@Composable
private fun LotSizeInfoItem(
    name: String,
    description: String,
    showDivider: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(horizontal = LessTheme.spacing.medium)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                style = LessTheme.typography.body16Regular,
                color = LessTheme.colors.textIconsBlack
            )
            Spacer(modifier = Modifier.height(LessTheme.spacing.xxxSmall))
            Text(
                text = description,
                style = LessTheme.typography.body14Regular,
                color = LessTheme.colors.textIconsGrey
            )
        }
        
        if (showDivider) {
            HorizontalDivider(
                color = LessTheme.colors.borderPrimary,
                thickness = 1.dp
            )
        }
    }
}
