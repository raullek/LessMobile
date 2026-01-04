package az.less.mobile.presentation.client.main.voucher.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_gift_48dp
import org.jetbrains.compose.resources.painterResource

/**
 * Empty state component for Voucher screen
 * Shown when user has no vouchers
 */
@Composable
fun VoucherEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Gift icon
        Image(
            painter = painterResource(Res.drawable.ic_gift_48dp),
            contentDescription = "Gift",
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        // Title
        Text(
            text = "You don't have vouchers",
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        // Description
        Text(
            text = "To earn voucher refer our app to your friends, you will 2 ₼ for each friend after his first transaction",
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsGrey,
            textAlign = TextAlign.Center
        )
    }
}
