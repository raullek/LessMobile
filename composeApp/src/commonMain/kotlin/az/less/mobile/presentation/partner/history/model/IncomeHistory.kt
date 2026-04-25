package az.less.mobile.presentation.partner.history.model

/**
 * Represents an income position (individual transaction)
 */
data class IncomePosition(
    val id: String,
    val customerName: String,
    val customerInitials: String, // e.g., "MP" for "Mahammadali Pashayev"
    val time: String, // e.g., "12:34"
    // TODO: Add branchName when branch-specific data is available from API
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
 * Represents a venue option for filtering
 */
data class VenueOption(
    val id: String,
    val name: String,
    val address: String
)
