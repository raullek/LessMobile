package az.less.mobile.presentation.main.offers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.ButtonVariant
import az.less.designsystem.components.DsButton
import az.less.designsystem.components.DsTextField

@Composable
fun HomeScreen() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Offers",
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack
        )
    }
//    var textFieldValue by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(LessTheme.spacing.medium)
//    ) {
//        DsTextField(
//            value = textFieldValue,
//            onValueChange = { textFieldValue = it },
//            modifier = Modifier.fillMaxWidth(),
//            label = "Label",
//            placeholder = "Placeholder",
//            onEndIconClick = { textFieldValue = "" },
//        )
//
//        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
//
//        DsButton(
//            modifier = Modifier.fillMaxWidth(),
//            text = "Button",
//            onClick = { /*TODO*/ },
//            variant = ButtonVariant.Primary
//        )
//    }
}

