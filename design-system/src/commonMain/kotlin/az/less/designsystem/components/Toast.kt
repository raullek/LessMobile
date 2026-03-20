package az.less.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import az.less.designsystem.base.LessTheme
import lessmobile.design_system.generated.resources.Res
import lessmobile.design_system.generated.resources.ic_delete_24dp
import org.jetbrains.compose.resources.vectorResource

enum class ToastType {
    Success,
    Error,
    Info,
    Warning
}

@Composable
fun LessToast(
    title: String,
    subtitle: String? = null,
    type: ToastType = ToastType.Success,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (type) {
        ToastType.Success -> LessTheme.colors.surfaceSuccess
        ToastType.Error -> LessTheme.colors.surfaceError
        ToastType.Info -> LessTheme.colors.surfaceInfo
        ToastType.Warning -> LessTheme.colors.surfaceWhite
    }

    val iconBackgroundColor = when (type) {
        ToastType.Success -> LessTheme.colors.textIconsSuccess
        ToastType.Error -> LessTheme.colors.textIconsError
        ToastType.Info -> LessTheme.colors.textIconsInfo
        ToastType.Warning -> LessTheme.colors.textIconsWarning
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LessTheme.radius.small))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = LessTheme.colors.borderPrimary,
                shape = RoundedCornerShape(LessTheme.radius.small)
            )
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 16.dp
            ),
        verticalAlignment = Alignment.Top
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_delete_24dp),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = LessTheme.colors.textIconsNested
            )
        }

        Spacer(modifier = Modifier.width(LessTheme.spacing.medium))

        // Message content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = LessTheme.typography.body14Regular,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }
    }
}

@Composable
fun AnimatedToast(
    visible: Boolean,
    title: String,
    subtitle: String? = null,
    type: ToastType = ToastType.Success,
    modifier: Modifier = Modifier,
    showGradientScrim: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    val gradientColor = when (type) {
        ToastType.Success -> LessTheme.colors.elementsPrimaryBrand
        ToastType.Error -> LessTheme.colors.textIconsError
        ToastType.Info -> LessTheme.colors.textIconsInfo
        ToastType.Warning -> LessTheme.colors.textIconsWarning
    }

    Box(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars),
        contentAlignment = Alignment.TopCenter
    ) {
        // Gradient scrim background
        if (showGradientScrim) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    gradientColor.copy(alpha = 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }

        // Toast content
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            LessToast(
                title = title,
                subtitle = subtitle,
                type = type,
                modifier = Modifier.padding(contentPadding)
            )
        }
    }
}
