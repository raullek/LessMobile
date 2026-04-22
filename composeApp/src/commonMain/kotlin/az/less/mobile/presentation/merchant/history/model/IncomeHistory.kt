package az.less.mobile.presentation.merchant.history.model

data class IncomePosition(
    val id: String,
    val customerName: String,
    val customerInitials: String,
    val time: String,
    val amount: String
)

data class IncomeHistory(
    val date: String,
    val incomeValue: String,
    val incomePositions: List<IncomePosition>
)
