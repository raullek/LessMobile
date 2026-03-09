package az.less.mobile.presentation.merchant.more.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * Enum representing cell identifiers in MerchMore screen
 */
enum class MerchCellId {
    Places,
    Notification,
    DarkMode,
    ContactUs,
    TermsOfService,
    SwitchToClient
}

/**
 * Model representing a cell item in MerchMore screen
 */
data class MerchMoreCellModel(
    val id: MerchCellId,
    val titleRes: StringResource,
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
    val titleRes: StringResource,
    val cells: List<MerchMoreCellModel>
)
