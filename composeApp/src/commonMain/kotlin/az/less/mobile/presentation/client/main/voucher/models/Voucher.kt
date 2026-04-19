package az.less.mobile.presentation.client.main.voucher.models

import az.less.mobile.data.remote.model.VoucherItemDto

data class Voucher(
    val id: String,
    val code: String,
    val title: String,
    val description: String,
    val type: String,
    val value: Double,
    val currency: String,
    val expiresAt: String,
    val minSubtotal: Double?,
    val maxDiscount: Double?
) {
    val formattedValue: String
        get() = when (type) {
            "percent" -> "${(value * 100).toInt()}%"
            "fixed" -> "${"%.2f".format(value)} $currency"
            else -> value.toString()
        }
}

fun VoucherItemDto.toDomain(): Voucher {
    return Voucher(
        id = id,
        code = code.orEmpty(),
        title = snapshot?.title.orEmpty(),
        description = snapshot?.description.orEmpty(),
        type = snapshot?.type.orEmpty(),
        value = snapshot?.value ?: 0.0,
        currency = snapshot?.currency.orEmpty(),
        expiresAt = expiresAt.orEmpty(),
        minSubtotal = snapshot?.minSubtotal,
        maxDiscount = snapshot?.maxDiscount
    )
}
