package az.less.mobile.presentation.client.main.more.root.models

import org.jetbrains.compose.resources.DrawableResource

/**
 * Enum representing cell identifiers in More screen
 */
enum class CellId {
    Account,
    PaymentMethods,
    Voucher,
    History,
    Settings,
    ContactUs,
    SignStore,
    TermsOfService,
    HowToUse,
    Notification
}

/**
 * Model representing a cell item in More screen
 */
data class MoreCellModel(
    val id: az.less.mobile.presentation.client.main.more.root.models.CellId,
    val title: String,
    val icon: DrawableResource? = null,
    val type: az.less.mobile.presentation.client.main.more.root.models.MoreCellType = _root_ide_package_.az.less.mobile.presentation.client.main.more.root.models.MoreCellType.Navigation,
    val showDivider: Boolean = true
)

/**
 * Type of cell interaction
 */
sealed class MoreCellType {
    data object Navigation : az.less.mobile.presentation.client.main.more.root.models.MoreCellType()
    data class Toggle(val checked: Boolean) : az.less.mobile.presentation.client.main.more.root.models.MoreCellType()
}

/**
 * Section model for grouping cells
 */
data class MoreSection(
    val title: String,
    val cells: List<az.less.mobile.presentation.client.main.more.root.models.MoreCellModel>
)

