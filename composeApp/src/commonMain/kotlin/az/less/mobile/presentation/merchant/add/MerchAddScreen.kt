package az.less.mobile.presentation.merchant.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme

@Composable
fun MerchAddScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundPrimary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Add",
            style = LessTheme.typography.body16Bold,
            color = LessTheme.colors.textIconsLightBrand
        )
    }
}
