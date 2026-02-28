package az.less.mobile.presentation.client.main.merchant.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.utils.formatOneDecimal
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_star_16dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun MerchantProfileInfoSection(
    merchantName: String,
    description: String,
    rating: Float,
    distance: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LessTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        Text(
            text = merchantName,
            style = LessTheme.typography.body16Semibold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        Text(
            text = description,
            style = LessTheme.typography.body14Regular,
            color = LessTheme.colors.textIconsGrey,
            textAlign = TextAlign.Center
        )

        if (rating > 0f || distance.isNotEmpty()) {
            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .background(LessTheme.colors.backgroundPrimary)
                    .padding(horizontal = LessTheme.spacing.medium, vertical = LessTheme.spacing.xSmall),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (rating > 0f) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_star_16dp),
                        contentDescription = "Rating",
                        tint = LessTheme.colors.textIconsWarning,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(LessTheme.spacing.xxSmall))
                    Text(
                        text = rating.formatOneDecimal(),
                        style = LessTheme.typography.body14Semibold,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
                if (rating > 0f && distance.isNotEmpty()) {
                    Text(
                        text = " \u2022 ",
                        style = LessTheme.typography.body14Semibold,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
                if (distance.isNotEmpty()) {
                    Text(
                        text = distance,
                        style = LessTheme.typography.body14Semibold,
                        color = LessTheme.colors.textIconsBlack
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(LessTheme.spacing.large))
    }
}
