package az.less.mobile.presentation.client.main.voucher.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import az.less.designsystem.base.LessTheme
import az.less.mobile.presentation.client.main.voucher.models.Voucher
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.voucher_discount
import lessmobile.composeapp.generated.resources.voucher_expiry_date
import lessmobile.composeapp.generated.resources.voucher_code_label
import org.jetbrains.compose.resources.stringResource

/**
 * Custom shape for voucher card with serrated/zigzag edges on top and bottom
 */
class VoucherTicketShape(
    private val teethHeight: Dp = 8.dp,
    private val teethWidth: Dp = 12.dp,
    private val cornerRadius: Dp = 16.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val teethHeightPx = with(density) { teethHeight.toPx() }
        val teethWidthPx = with(density) { teethWidth.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }

        val path = Path().apply {
            moveTo(cornerRadiusPx, 0f)

            var x = cornerRadiusPx
            var goingDown = true
            while (x < size.width - cornerRadiusPx) {
                val nextX = minOf(x + teethWidthPx / 2, size.width - cornerRadiusPx)
                val y = if (goingDown) teethHeightPx else 0f
                lineTo(nextX, y)
                x = nextX
                goingDown = !goingDown
            }

            lineTo(size.width - cornerRadiusPx, 0f)
            arcTo(
                rect = Rect(
                    size.width - cornerRadiusPx * 2, 0f,
                    size.width, cornerRadiusPx * 2
                ),
                startAngleDegrees = 270f, sweepAngleDegrees = 90f, forceMoveTo = false
            )

            lineTo(size.width, size.height - cornerRadiusPx)
            arcTo(
                rect = Rect(
                    size.width - cornerRadiusPx * 2, size.height - cornerRadiusPx * 2,
                    size.width, size.height
                ),
                startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
            )

            x = size.width - cornerRadiusPx
            goingDown = true
            while (x > cornerRadiusPx) {
                val nextX = maxOf(x - teethWidthPx / 2, cornerRadiusPx)
                val y = if (goingDown) size.height - teethHeightPx else size.height
                lineTo(nextX, y)
                x = nextX
                goingDown = !goingDown
            }

            lineTo(cornerRadiusPx, size.height)
            arcTo(
                rect = Rect(
                    0f, size.height - cornerRadiusPx * 2,
                    cornerRadiusPx * 2, size.height
                ),
                startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
            )

            lineTo(0f, cornerRadiusPx)
            arcTo(
                rect = Rect(0f, 0f, cornerRadiusPx * 2, cornerRadiusPx * 2),
                startAngleDegrees = 180f, sweepAngleDegrees = 90f, forceMoveTo = false
            )

            close()
        }

        return Outline.Generic(path)
    }
}

@Composable
fun VoucherCard(
    voucher: Voucher,
    modifier: Modifier = Modifier
) {
    val dashedLineColor = LessTheme.colors.textIconsBrand
    val backgroundColor = LessTheme.colors.backgroundPrimary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(VoucherTicketShape())
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LessTheme.spacing.medium)
                .padding(top = LessTheme.spacing.large, bottom = LessTheme.spacing.large)
        ) {
            // Title
            Text(
                text = voucher.title,
                style = LessTheme.typography.body16Semibold,
                color = LessTheme.colors.textIconsBlack
            )

            if (voucher.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))
                Text(
                    text = voucher.description,
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.small))

            // Discount row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.voucher_discount),
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey
                )
                Text(
                    text = voucher.formattedValue,
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))

            // Expiry date row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.voucher_expiry_date),
                    style = LessTheme.typography.body14Medium,
                    color = LessTheme.colors.textIconsGrey
                )
                Text(
                    text = voucher.expiresAt.take(10),
                    style = LessTheme.typography.body14Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Dashed separator line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .drawBehind {
                        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                        drawLine(
                            color = dashedLineColor,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 2f,
                            pathEffect = pathEffect
                        )
                    }
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            // Voucher Code section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(LessTheme.radius.small))
                    .border(
                        width = 1.dp,
                        color = LessTheme.colors.backgroundSecond,
                        shape = RoundedCornerShape(LessTheme.radius.small)
                    )
                    .padding(vertical = LessTheme.spacing.small),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.voucher_code_label),
                    style = LessTheme.typography.caption12Medium,
                    color = LessTheme.colors.textIconsGrey
                )
                Spacer(modifier = Modifier.height(LessTheme.spacing.xxSmall))
                Text(
                    text = voucher.code,
                    style = LessTheme.typography.body16Semibold,
                    color = LessTheme.colors.textIconsBlack
                )
            }
        }
    }
}
