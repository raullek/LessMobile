package az.less.mobile.presentation.merchant.more.model

import org.jetbrains.compose.resources.DrawableResource

/**
 * Enum representing cell identifiers in MerchMore screen
 */
enum class MerchCellId {
    Places,
    Notification,
    DarkMode,
    ContactUs,
    TermsOfService
}

/**
 * Model representing a cell item in MerchMore screen
 */
data class MerchMoreCellModel(
    val id: MerchCellId,
    val title: String,
    val icon: DrawableResource? = null,
    val type: MerchMoreCellType = MerchMoreCellType.Navigation,
    val showDivider: Boolean = true
)

/**
 * Type of cell interaction
 */
sealed class MerchMoreCellType {
    data object Navigation : MerchMoreCellType()
    data class Toggle(val checked: Boolean) : MerchMoreCellType()
}

/**
 * Section model for grouping cells
 */
data class MerchMoreSection(
    val title: String,
    val cells: List<MerchMoreCellModel>
)
