package az.less.mobile.presentation.client.main.offers.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ill_box_placeholder
import lessmobile.composeapp.generated.resources.ill_category_placeholder
import org.jetbrains.compose.resources.painterResource

@Composable
fun SpecialDiscountCard(
    item: SpecialDiscountItem,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(173.dp)
            .clip(RoundedCornerShape(LessTheme.radius.medium))
            .clickable { onClick() }
    ) {

        SubcomposeAsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            error = {
                Image(
                    painter = painterResource(Res.drawable.ill_box_placeholder),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(LessTheme.size.huge)
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        )
                    )
                )
                .align(Alignment.BottomStart)
                .padding(
                    start = LessTheme.spacing.medium,
                    end = LessTheme.spacing.medium,
                    bottom = LessTheme.spacing.large
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = item.title,
                style = LessTheme.typography.body16Semibold,
                color = Color.White
            )

            Text(
                text = item.description,
                style = LessTheme.typography.body14Regular,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
