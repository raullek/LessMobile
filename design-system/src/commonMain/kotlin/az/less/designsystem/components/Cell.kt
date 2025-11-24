package az.less.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

sealed class CellType {
    data class Navigation(
        val onClick: () -> Unit,
        val trailingIcon: DrawableResource? = null
    ) : CellType()
    
    data class Toggle(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit
    ) : CellType()
    
    data class CheckBox(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit
    ) : CellType()
    
    data object Static : CellType()
}

data class CellLeadingContent(
    val icon: DrawableResource? = null,
    val iconTint: Color? = null,
    val backgroundColor: Color? = null
)

@Composable
fun DsCell(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingContent: CellLeadingContent? = null,
    type: CellType = CellType.Static,
    enabled: Boolean = true,
    showDivider: Boolean = true
) {
    val clickableModifier = when (type) {
        is CellType.Navigation -> Modifier.clickable(
            enabled = enabled,
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = type.onClick
        )
        else -> Modifier
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = LessTheme.size.huge)
                .then(clickableModifier)
                .padding(
                    vertical = LessTheme.spacing.xSmall
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small)
        ) {
            // Leading content (icon)
            leadingContent?.icon?.let { iconRes ->
                Box(
                    modifier = Modifier
                        .size(LessTheme.size.medium)
                        .clip(CircleShape)
                        .background(leadingContent.backgroundColor ?: Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = leadingContent.iconTint ?: LessTheme.colors.textIconsBlack,
                        modifier = Modifier.size(LessTheme.size.medium)
                    )
                }
            }

            // Title and subtitle
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxxSmall)
            ) {
                Text(
                    text = title,
                    style = LessTheme.typography.body16Medium,
                    color = if (enabled) {
                        LessTheme.colors.textIconsBlack
                    } else {
                        LessTheme.colors.textIconsThird
                    }
                )
                
                subtitle?.let {
                    Text(
                        text = it,
                        style = LessTheme.typography.body14Regular,
                        color = LessTheme.colors.textIconsGrey
                    )
                }
            }

            // Trailing content
            when (type) {
                is CellType.Navigation -> {
                    type.trailingIcon?.let { iconRes ->
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            tint = LessTheme.colors.textIconsThird,
                            modifier = Modifier.size(LessTheme.size.medium)
                        )
                    }
                }
                
                is CellType.Toggle -> {
                    Switch(
                        checked = type.checked,
                        onCheckedChange = type.onCheckedChange,
                        enabled = enabled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = LessTheme.colors.textIconsNested,
                            checkedTrackColor = LessTheme.colors.elementsPrimaryBrand,
                            uncheckedThumbColor = LessTheme.colors.textIconsNested,
                            uncheckedTrackColor = LessTheme.colors.elementsThirdElement,
                            uncheckedBorderColor = Color.Transparent,
                            disabledCheckedThumbColor = LessTheme.colors.textIconsThird,
                            disabledCheckedTrackColor = LessTheme.colors.elementsThirdElement,
                            disabledUncheckedThumbColor = LessTheme.colors.textIconsThird,
                            disabledUncheckedTrackColor = LessTheme.colors.elementsSecondaryElement
                        )
                    )
                }
                
                is CellType.CheckBox -> {
                    DsCheckBox(
                        checked = type.checked,
                        onCheckedChange = { type.onCheckedChange(it) },
                        enabled = enabled
                    )
                }
                
                is CellType.Static -> {
                    // No trailing content
                }
            }
        }
        
        // Bottom divider
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(start = LessTheme.spacing.xxLarge)
                    .background(LessTheme.colors.borderPrimary)
            )
        }
    }
}

@Composable
fun DsSectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title.uppercase(),
        style = LessTheme.typography.caption12Bold,
        color = LessTheme.colors.textIconsThird,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = LessTheme.spacing.medium,
                vertical = LessTheme.spacing.xxSmall
            )
    )
}

