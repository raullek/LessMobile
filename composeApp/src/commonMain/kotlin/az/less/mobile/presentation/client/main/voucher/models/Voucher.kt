package az.less.mobile.presentation.client.main.voucher.models

data class Voucher(
    val id: String,
    val title: String,
    val amount: String,
    val expiryDate: String,
    val loyaltyCode: String
)
