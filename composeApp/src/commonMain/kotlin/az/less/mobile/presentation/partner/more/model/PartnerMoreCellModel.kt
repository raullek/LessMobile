package az.less.mobile.presentation.partner.more.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class PartnerMoreCellId {
    Notification,
    DarkMode,
    ContactUs,
    TermsOfService,
    SwitchToClient
}

data class PartnerMoreCellModel(
    val id: PartnerMoreCellId,
    val titleRes: StringResource,
    val icon: DrawableResource? = null,
    val type: PartnerMoreCellType = PartnerMoreCellType.Navigation,
    val showDivider: Boolean = true
)

sealed class PartnerMoreCellType {
    data object Navigation : PartnerMoreCellType()
    data class Toggle(val checked: Boolean) : PartnerMoreCellType()
}

data class PartnerMoreSection(
    val titleRes: StringResource,
    val cells: List<PartnerMoreCellModel>
)
