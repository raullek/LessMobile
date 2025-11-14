package az.less.mobile.presentation.main.orders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import az.less.designsystem.base.LessTheme

@Composable
fun OrdersScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Orders",
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack
        )
    }
}

