package az.less.mobile.presentation.main.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.CellLeadingContent
import az.less.designsystem.components.CellType
import az.less.designsystem.components.DsCell
import az.less.designsystem.components.DsSectionHeader
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_more_24dp
import lessmobile.composeapp.generated.resources.ic_offers_24dp
import lessmobile.composeapp.generated.resources.ic_orders_24dp
import lessmobile.composeapp.generated.resources.ic_saved_24dp

@Composable
fun MoreScreen() {
    var notificationEnabled by remember { mutableStateOf(true) }
    var marketingEnabled by remember { mutableStateOf(false) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundPrimary)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        
        DsCell(
            title = "Offers",
            leadingContent = CellLeadingContent(
                icon = Res.drawable.ic_offers_24dp
            ),
            type = CellType.Navigation(
                onClick = { /* Navigate */ }
            )
        )
        
        DsCell(
            title = "Explore",
            subtitle = "Discover new items",
            leadingContent = CellLeadingContent(
                icon = Res.drawable.ic_explore_24dp
            ),
            type = CellType.Navigation(
                onClick = { /* Navigate */ }
            )
        )
        
        DsCell(
            title = "Orders",
            leadingContent = CellLeadingContent(
                icon = Res.drawable.ic_orders_24dp
            ),
            type = CellType.Navigation(
                onClick = { /* Navigate */ }
            ),
            showDivider = false
        )

        
        DsCell(
            title = "Notifications",
            subtitle = "Enable push notifications",
            leadingContent = CellLeadingContent(
                icon = Res.drawable.ic_saved_24dp
            ),
            type = CellType.Toggle(
                checked = notificationEnabled,
                onCheckedChange = { notificationEnabled = it }
            )
        )
        
        DsCell(
            title = "Dark Mode",
            leadingContent = CellLeadingContent(
                icon = Res.drawable.ic_more_24dp
            ),
            type = CellType.Toggle(
                checked = darkModeEnabled,
                onCheckedChange = { darkModeEnabled = it }
            ),
            showDivider = false
        )

        
        DsCell(
            title = "Marketing Emails",
            subtitle = "Receive promotional offers and updates",
            type = CellType.CheckBox(
                checked = marketingEnabled,
                onCheckedChange = { marketingEnabled = it }
            )
        )
        
        DsCell(
            title = "Terms & Conditions",
            subtitle = "I agree to the terms and conditions",
            type = CellType.CheckBox(
                checked = false,
                onCheckedChange = { }
            ),
            showDivider = false
        )
        
        // Section with Static cells
        DsSectionHeader(title = "Static Examples")
        
        DsCell(
            title = "Version",
            subtitle = "1.0.0",
            type = CellType.Static
        )
        
        DsCell(
            title = "Build Number",
            subtitle = "12345",
            leadingContent = CellLeadingContent(
                icon = Res.drawable.ic_more_24dp
            ),
            type = CellType.Static,
            showDivider = false
        )
    }
}
