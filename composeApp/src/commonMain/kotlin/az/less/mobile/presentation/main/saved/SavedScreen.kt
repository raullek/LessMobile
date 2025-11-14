package az.less.mobile.presentation.main.saved

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import az.less.designsystem.base.LessTheme

@Composable
fun SavedScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Saved",
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack
        )
    }
}

