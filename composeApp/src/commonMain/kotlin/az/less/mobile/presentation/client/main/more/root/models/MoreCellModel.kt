package az.less.mobile.presentation.client.main.more.root.models

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

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
    Notification,
    Location
}

/**
 * Model representing a cell item in More screen
 */
data class MoreCellModel(
    val id: CellId,
    val titleRes: StringResource,
    val subtitleRes: StringResource? = null,
    val icon: DrawableResource? = null,
    val type: MoreCellType = MoreCellType.Navigation,
    val showDivider: Boolean = true
)

/**
 * Type of cell interaction
 */
sealed class MoreCellType {
    data object Navigation : MoreCellType()
    data class Toggle(val checked: Boolean) : MoreCellType()
}

/**
 * Section model for grouping cells
 */
data class MoreSection(
    val titleRes: StringResource,
    val cells: List<MoreCellModel>
)
