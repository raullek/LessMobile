package az.less.mobile.presentation.main.offers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.main.offers.components.CategoryCard
import az.less.mobile.presentation.main.offers.components.OfferCard
import az.less.mobile.presentation.main.offers.components.OffersHeader
import az.less.mobile.presentation.main.offers.components.SearchFilterBar
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * Stateful OffersScreen that connects to ViewModel
 * This is the entry point used by navigation
 */
@Composable
fun OffersScreen(
    viewModel: OffersViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.collectAsState()
    
    // Collect side effects for navigation
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is OffersSideEffect.NavigateToOfferDetail -> {
                // Handle navigation to offer detail
                // navController.navigate("offer_detail/${sideEffect.offerId}")
            }
            is OffersSideEffect.NavigateToSearch -> {
                // Handle navigation to search screen
                // navController.navigate("search")
            }
            is OffersSideEffect.ShowError -> {
                // Show error snackbar
            }
        }
    }
    
    // Render the stateless UI
    OffersScreenContent(
        state = state,
        onIntent = viewModel::onIntent
    )
}

/**
 * Stateless OffersScreen UI implementation
 * Pure UI that receives state and emits intents
 */
@Composable
fun OffersScreenContent(
    state: OffersState,
    onIntent: (OffersIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LessTheme.colors.backgroundSecond)
    ) {
        // Spacing from top
        item(key = "top_spacing") {
            Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))
        }
        
        // Header with User Avatar, Name, and Notification Buttons
        item(key = "header") {
            OffersHeader(
                userName = state.userName,
                onNotificationClick = { /* Handle notification */ },
                onMessageClick = { /* Handle message */ },
                userAvatarUrl = state.userAvatarUrl
            )
        }
        
        item(key = "header_spacing") {
            Spacer(modifier = Modifier.height(LessTheme.spacing.small))
        }
        
        // Search and Filter Bar
        item(key = "search_filter") {
            SearchFilterBar(
                searchQuery = state.searchQuery,
                onSearchClick = {
                    onIntent(OffersIntent.OnSearchClicked)
                },
                onFilterClick = { /* Handle filter */ }
            )
        }
        
        item(key = "search_spacing") {
            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
        }
        
        // Horizontal Categories Section
        item(key = "categories") {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                contentPadding = PaddingValues(horizontal = LessTheme.spacing.medium)
            ) {
                items(
                    items = state.categories,
                    key = { category -> category.id }
                ) { category ->
                    CategoryCard(
                        title = category.title,
                        onClick = {
                            onIntent(OffersIntent.OnCategorySelected(category.id))
                        },
                        imageUrl = category.imageUrl,
                        testImage = category.testImage
                    )
                }
            }
        }
        
        item(key = "categories_spacing") {
            Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge + LessTheme.spacing.small)) // 32 + 12 = 44dp
        }
        
        // Dynamic Offer Sections (Top rated, Top picks, etc.)
        state.offerSections.forEach { section ->
            // Section Header
            item(key = "${section.id}_header") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LessTheme.spacing.medium)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = section.title,
                            style = LessTheme.typography.body16Semibold,
                            color = LessTheme.colors.textIconsBlack
                        )
                        
                        if (section.showSeeAll) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "See all",
                                    style = LessTheme.typography.body16Semibold,
                                    color = LessTheme.colors.textIconsBrand
                                )
                                Icon(
                                    painter = painterResource(Res.drawable.ic_chevron_right_24dp),
                                    contentDescription = "See all",
                                    tint = LessTheme.colors.textIconsBrand,
                                    modifier = Modifier.padding(start = LessTheme.spacing.xxxSmall)
                                )
                            }
                        }
                    }
                }
            }
            
            item(key = "${section.id}_spacing") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.small + LessTheme.spacing.xSmall)) // 12 + 8 = 20dp
            }
            
            // Section Items - Horizontal Scrolling Cards
            item(key = "${section.id}_items") {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.medium),
                    contentPadding = PaddingValues(horizontal = LessTheme.spacing.medium)
                ) {
                    items(
                        items = section.items,
                        key = { item -> "${section.id}_${item.id}" }
                    ) { offerItem ->
                        OfferCard(
                            offerItem = offerItem,
                            onClick = {
                                onIntent(OffersIntent.OnOfferItemClicked(offerItem.id))
                            }
                        )
                    }
                }
            }
            
            item(key = "${section.id}_bottom_spacing") {
                Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge + LessTheme.spacing.small)) // 32 + 12 = 44dp
            }
        }
        
        // Bottom spacing
        item(key = "bottom_spacing") {
            Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))
        }
    }
}
