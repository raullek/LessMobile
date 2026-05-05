package az.less.mobile.presentation.client.main.merchant.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.rememberDsBackIconPainter
import coil3.compose.AsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_saved_24dp
import lessmobile.composeapp.generated.resources.ill_box_placeholder
import lessmobile.composeapp.generated.resources.ill_venue_placeholder
import org.jetbrains.compose.resources.painterResource

private val ExpandedHeight = 240.dp
private val CollapsedHeight = 56.dp
private val LogoSize = 80.dp
// Half of the logo overhangs the hero image bottom, the other half sits on the
// surface below — matches the original `MerchantProfileHeroSection` layout where
// the hero image was 200.dp inside a 240.dp box, leaving the logo straddling the
// boundary.
private val LogoOverhang = LogoSize / 2

/**
 * Material3 collapsing top-app-bar that hosts the merchant hero image, logo and overlay
 * back/favorite buttons. As the user scrolls the attached list, the toolbar shrinks from
 * [ExpandedHeight] down to [CollapsedHeight]; hero + logo fade out and the icon button
 * tints lerp from white (over the photo) to the on-surface color (over the solid bar).
 *
 * The merchant name is shown in the collapsed bar only after the in-content title has
 * scrolled out of view: the host passes a [LazyListState] so the toolbar can detect when
 * the first item (the info section that contains the in-content title) is no longer the
 * first visible item, and cross-fade the toolbar title in.
 *
 * The driving signal is [TopAppBarScrollBehavior.state.collapsedFraction]; the host screen
 * is responsible for wiring `Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)`
 * onto the scrolling list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingMerchantToolbar(
    title: String,
    heroImageUrl: String?,
    merchantLogoUrl: String?,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    showFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    val density = LocalDensity.current
    val statusBarPx = WindowInsets.statusBars.getTop(density)
    val statusBarDp = with(density) { statusBarPx.toDp() }

    val expandedPx = with(density) { (ExpandedHeight + statusBarDp).toPx() }
    val collapsedPx = with(density) { (CollapsedHeight + statusBarDp).toPx() }
    val limit = -(expandedPx - collapsedPx)

    SideEffect {
        if (scrollBehavior.state.heightOffsetLimit != limit) {
            scrollBehavior.state.heightOffsetLimit = limit
        }
    }

    val collapsedFraction = scrollBehavior.state.collapsedFraction
    val currentHeightPx = expandedPx + scrollBehavior.state.heightOffset
    val currentHeightDp = with(density) { currentHeightPx.toDp() }

    val iconColor = lerp(
        Color.White,
        LessTheme.colors.textIconsBlack,
        collapsedFraction
    )
    val iconBgColor = Color(0x80171A1C).copy(alpha = (1f - collapsedFraction))

    // Toolbar title is only useful once the in-content title has scrolled past.
    // Item index 0 is the info section that owns the merchant name.
    val showTitle by remember(lazyListState) {
        derivedStateOf { lazyListState.firstVisibleItemIndex >= 1 }
    }

    // Hero image is shorter than the toolbar by [LogoOverhang] so the logo sits
    // half on the photo and half on the surface below — same offset as the
    // original `MerchantProfileHeroSection`.
    val heroHeightDp = (currentHeightDp - LogoOverhang).coerceAtLeast(0.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(currentHeightDp)
            .background(LessTheme.colors.backgroundSecond.copy(alpha = collapsedFraction))
    ) {
        // Hero image — fades out on collapse, leaves logo overhang space at the bottom.
        AsyncImage(
            model = heroImageUrl,
            contentDescription = "Merchant hero image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(heroHeightDp)
                .alpha(1f - collapsedFraction),
            error = painterResource(Res.drawable.ill_box_placeholder),
            placeholder = painterResource(Res.drawable.ill_box_placeholder)
        )

        // Merchant logo — straddles the hero/surface boundary, fades out on collapse.
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .alpha(1f - collapsedFraction)
                .size(LogoSize)
                .shadow(
                    elevation = LessTheme.elevation.medium,
                    shape = RoundedCornerShape(LessTheme.radius.medium)
                )
                .clip(RoundedCornerShape(LessTheme.radius.medium))
                .background(LessTheme.colors.backgroundPrimary),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = merchantLogoUrl,
                contentDescription = "Merchant logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(LogoSize - 4.dp)
                    .clip(RoundedCornerShape(LessTheme.radius.medium)),
                error = painterResource(Res.drawable.ill_venue_placeholder),
                placeholder = painterResource(Res.drawable.ill_venue_placeholder)
            )
        }

        // Collapsed-state title — appears with fade only after the content title is offscreen.
        AnimatedVisibility(
            visible = showTitle,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = statusBarDp)
                .fillMaxWidth()
                .height(CollapsedHeight)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 64.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }

        // Back button — pinned to the top, tint and background lerp on collapse.
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = LessTheme.spacing.small, top = statusBarDp + 6.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(iconBgColor)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = rememberDsBackIconPainter(),
                contentDescription = "Back",
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        if (showFavorite) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = LessTheme.spacing.small, top = statusBarDp + 6.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBgColor)
                    .clickable { onFavoriteClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_saved_24dp),
                    contentDescription = "Favorite",
                    tint = if (isFavorite) LessTheme.colors.textIconsBrand else iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
