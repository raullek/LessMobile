package az.less.mobile.presentation.main.offers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_bubble_question_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Search bar with filter button component for Offers screen
 * Based on Figma design with search icon and placeholder text, plus filter button on right
 */
@Composable
fun SearchFilterBar(
    searchQuery: String,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search Bar
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.backgroundPrimary)
                .clickable { onSearchClick() }
                .padding(LessTheme.spacing.small),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bubble_question_24dp),
                    contentDescription = "Search",
                    tint = LessTheme.colors.textIconsGrey,
                    modifier = Modifier.size(LessTheme.size.medium)
                )
                
                Spacer(modifier = Modifier.width(LessTheme.spacing.small + LessTheme.spacing.xxxSmall)) // 12 + 2 = 14dp
                
                Text(
                    text = if (searchQuery.isEmpty()) "Drinks, chocolates and snacks" else searchQuery,
                    style = LessTheme.typography.body14Medium,
                    color = if (searchQuery.isEmpty()) 
                        LessTheme.colors.textIconsThird 
                    else 
                        LessTheme.colors.textIconsBlack
                )
            }
        }
        
        Spacer(modifier = Modifier.width(LessTheme.spacing.xxSmall + LessTheme.spacing.xxxSmall)) // 4 + 2 = 6dp
        
        // Filter Button
        Box(
            modifier = Modifier
                .size(LessTheme.size.xxLarge)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.backgroundPrimary)
                .clickable { onFilterClick() }
                .padding(LessTheme.spacing.xxSmall),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_bubble_question_24dp),
                contentDescription = "Filter",
                tint = LessTheme.colors.textIconsBlack,
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }
    }
}

