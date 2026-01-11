package az.less.mobile.presentation.client.main.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.explore.models.OfferSlot
import az.less.mobile.presentation.client.main.offers.components.OfferCard
import az.less.mobile.presentation.client.main.offers.models.OfferItem
import az.less.mobile.presentation.client.main.offers.models.OfferMerchant

/**
 * Horizontal LazyRow displaying merchant offer slots
 * Shown when a marker is selected on the map
 * Uses the same OfferCard component as OffersScreen
 */
@Composable
fun MerchantSlotsRow(
    slots: List<az.less.mobile.presentation.client.main.explore.models.OfferSlot>,
    merchantName: String = "",
    modifier: Modifier = Modifier,
    onSlotClick: (String) -> Unit = {}
) {
    if (slots.isEmpty()) return
    
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = LessTheme.spacing.huge)
            .background(
                color = Color.Transparent, // Transparent background
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(LessTheme.spacing.small),
        contentPadding = PaddingValues(horizontal = LessTheme.spacing.medium)
    ) {
        items(
            items = slots,
            key = { it.id }
        ) { slot ->
            // Convert OfferSlot to OfferItem for OfferCard
            // Format pickupTime: "17:00 - 18:00" -> "Pick up from 17:00 to 18:00"
            val formattedPickupTime = if (slot.pickupTime.contains(" - ")) {
                slot.pickupTime.replace(" - ", " to ").let { 
                    if (!it.startsWith("Pick up from ")) "Pick up from $it" else it
                }
            } else {
                "Pick up from ${slot.pickupTime}"
            }
            
            val offerItem = OfferItem(
                id = slot.id,
                title = slot.title,
                imageUrl = slot.imageUrl,
                imageBgColor = "#fff2eb",
                originalPrice = "", // No original price for slots
                currentPrice = slot.price,
                bagType = "Surprise Bag",
                category = "Mixed",
                pickupTime = formattedPickupTime,
                merchant = OfferMerchant(
                    id = slot.id,
                    name = merchantName,
                    location = "",
                    rating = 4.5f
                )
            )

            OfferCard(
                offerItem = offerItem,
                onClick = { onSlotClick(slot.id) }
            )
        }
    }
}

