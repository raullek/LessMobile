package az.less.mobile.presentation.merchant.history.model

/**
 * Represents an income position (individual transaction)
 */
data class IncomePosition(
    val id: String,
    val customerName: String,
    val customerInitials: String, // e.g., "MP" for "Mahammadali Pashayev"
    val time: String, // e.g., "12:34"
    val branchName: String, // e.g., "Ahmdali mcdonalds"
    val amount: String // e.g., "2.24"
)

/**
 * Represents income history for a specific date
 * Format: incomeHistory{date, incomeValue, [incomePositions]}
 */
data class IncomeHistory(
    val date: String, // e.g., "2024-09-10" or formatted "Today", "10 Mart"
    val incomeValue: String, // e.g., "2 026,25" - total income for this date
    val incomePositions: List<IncomePosition>
)

/**
 * Represents a month option for filtering
 */
data class MonthOption(
    val id: String,
    val displayName: String, // e.g., "September", "October"
    val value: String // e.g., "2024-09"
)

/**
 * Represents a branch option for filtering
 */
data class BranchOption(
    val id: String,
    val name: String // e.g., "All branches", "Ahmdali mcdonalds"
)

