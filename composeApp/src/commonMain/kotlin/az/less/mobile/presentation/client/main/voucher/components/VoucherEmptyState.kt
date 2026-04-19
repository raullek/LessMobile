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
import lessmobile.composeapp.generated.resources.voucher_empty_title
import lessmobile.composeapp.generated.resources.voucher_empty_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun VoucherEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_gift_48dp),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

        Text(
            text = stringResource(Res.string.voucher_empty_title),
            style = LessTheme.typography.title24Bold,
            color = LessTheme.colors.textIconsBlack,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.xSmall))

        Text(
            text = stringResource(Res.string.voucher_empty_description),
            style = LessTheme.typography.body16Medium,
            color = LessTheme.colors.textIconsGrey,
            textAlign = TextAlign.Center
        )
    }
}
