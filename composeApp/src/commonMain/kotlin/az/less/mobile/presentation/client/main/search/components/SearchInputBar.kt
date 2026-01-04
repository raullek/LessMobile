package az.less.mobile.presentation.client.main.search.components

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_filter_24dp
import lessmobile.composeapp.generated.resources.ic_search_24dp
import org.jetbrains.compose.resources.painterResource

/**
 * Search input bar component for Search screen
 * Based on Figma design with search icon, text input, and filter button
 */
@Composable
fun SearchInputBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onMapClicked: () -> Unit,
    modifier: Modifier = Modifier,
    autoFocus: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }

    // Request focus when composable is first launched
    LaunchedEffect(Unit) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search Bar with Material TextField
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            modifier = Modifier
                .weight(1f)
                .size(LessTheme.size.xxLarge)
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    text = "Search...",
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsThird
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_search_24dp),
                    contentDescription = "Search",
                    tint = LessTheme.colors.textIconsGrey,
                    modifier = Modifier.size(LessTheme.size.medium)
                )
            },
            textStyle = LessTheme.typography.body14Medium.copy(
                color = LessTheme.colors.textIconsBlack
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            singleLine = true,
            shape = RoundedCornerShape(LessTheme.radius.medium),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = LessTheme.colors.elementsPrimaryElement,
                unfocusedContainerColor = LessTheme.colors.elementsPrimaryElement,
                disabledContainerColor = LessTheme.colors.elementsPrimaryElement,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = LessTheme.colors.textIconsBlack
            )
        )
        
        Spacer(modifier = Modifier.width(LessTheme.spacing.xxSmall + LessTheme.spacing.xxxSmall)) // 6dp
        
        // Filter Button
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.elementsPrimaryElement)
                .clickable { onMapClicked() }
                .padding(LessTheme.spacing.xxSmall),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_explore_24dp),
                contentDescription = "Filter",
                tint = LessTheme.colors.textIconsBrand,
                modifier = Modifier.size(LessTheme.size.medium)
            )
        }
    }
}

