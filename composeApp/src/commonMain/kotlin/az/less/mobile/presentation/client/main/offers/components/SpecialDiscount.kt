package az.less.mobile.presentation.client.main.offers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem

/**
 * Special Discount Pager with page indicators
 * Uses HorizontalPager for page-by-page navigation
 */
@Composable
fun SpecialDiscountPager(
    items: List<az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem>,
    onItemClick: (az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { items.size })

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(LessTheme.spacing.small) // 12px gap as per design
    ) {
        // Horizontal Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = LessTheme.spacing.medium,
            contentPadding = PaddingValues(horizontal = LessTheme.spacing.medium)
        ) { page ->
            _root_ide_package_.az.less.mobile.presentation.client.main.offers.components.SpecialDiscountCard(
                item = items[page],
                onClick = { onItemClick(items[page]) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Page Indicators
        _root_ide_package_.az.less.mobile.presentation.client.main.offers.components.PageIndicators(
            pageCount = items.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Page Indicators component showing dots for each page
 * Active dot is fully opaque, others are 30% opacity
 */
@Composable
fun PageIndicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) {
                            LessTheme.colors.textIconsBlack
                        } else {
                            LessTheme.colors.textIconsBlack.copy(alpha = 0.3f) // 30% opacity
                        }
                    )
            )

            if (index < pageCount - 1) {
                Spacer(modifier = Modifier.width(LessTheme.spacing.xSmall))
            }
        }
    }
}