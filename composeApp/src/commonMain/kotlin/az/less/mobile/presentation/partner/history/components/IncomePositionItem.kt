package az.less.mobile.presentation.partner.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.partner.history.model.IncomePosition

/**
 * Income position item component displaying customer info, time/location, and amount
 * Based on Figma design
 */
@Composable
fun IncomePositionItem(
    position: IncomePosition,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LessTheme.spacing.medium,
                    vertical = LessTheme.spacing.small
                ),
            horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small),
            verticalAlignment = Alignment.Top
        ) {
            // Avatar with initials
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = LessTheme.colors.elementsPrimaryElement,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = position.customerInitials,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey
                )
            }
            
            // Customer info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.xxSmall)
            ) {
                // Customer name
                Text(
                    text = position.customerName,
                    style = LessTheme.typography.body16Medium,
                    color = LessTheme.colors.textIconsBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Time
                // TODO: Add branch name when branch-specific data is available from API
                Text(
                    text = position.time,
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsGrey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Amount
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Amount in green
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "+${position.amount.split(".")[0]}",
                        style = LessTheme.typography.body16Medium,
                        color = LessTheme.colors.textIconsSuccess
                    )
                    Text(
                        text = ",${position.amount.split(".").getOrNull(1) ?: "00"}",
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsSuccess
                    )
                    Text(
                        text = " ₼",
                        style = LessTheme.typography.body14Medium,
                        color = LessTheme.colors.textIconsSuccess
                    )
                }
            }
        }
        
        // Divider
        HorizontalDivider(
            color = LessTheme.colors.backgroundSecond,
            thickness = 1.dp
        )
    }
}

