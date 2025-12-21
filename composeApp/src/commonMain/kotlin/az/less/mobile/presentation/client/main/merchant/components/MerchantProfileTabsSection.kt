package az.less.mobile.presentation.client.main.merchant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.merchant.MerchantProfileTab

/**
 * Tabs section (Offers / Reviews)
 */
@Composable
fun MerchantProfileTabsSection(
    selectedTab: MerchantProfileTab,
    onTabSelected: (MerchantProfileTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(MerchantProfileTab.OFFERS, MerchantProfileTab.REVIEWS)
    val selectedIndex = tabs.indexOf(selectedTab)

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = LessTheme.colors.backgroundSecond,
        contentColor = LessTheme.colors.textIconsBrand,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                color = LessTheme.colors.textIconsBrand
            )
        },
        divider = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LessTheme.colors.borderPrimary)
            )
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = when (tab) {
                            MerchantProfileTab.OFFERS -> "Offers"
                            MerchantProfileTab.REVIEWS -> "Reviews"
                        },
                        style = LessTheme.typography.body16Semibold,
                        color = if (selectedIndex == index)
                            LessTheme.colors.textIconsBrand
                        else
                            LessTheme.colors.textIconsGrey
                    )
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
}
