package az.less.mobile.presentation.client.reserve

import androidx.compose.runtime.Composable
import az.less.designsystem.components.DsListBottomSheet
import az.less.designsystem.components.ListBottomSheetItem
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.contact_facebook
import lessmobile.composeapp.generated.resources.contact_instagram
import lessmobile.composeapp.generated.resources.contact_support_title
import lessmobile.composeapp.generated.resources.contact_telegram
import lessmobile.composeapp.generated.resources.contact_tiktok
import lessmobile.composeapp.generated.resources.contact_whatsapp
import lessmobile.composeapp.generated.resources.ic_chevron_right_24dp
import lessmobile.composeapp.generated.resources.ic_social_facebook_24dp
import lessmobile.composeapp.generated.resources.ic_social_instagram_24dp
import lessmobile.composeapp.generated.resources.ic_social_telegram_24dp
import lessmobile.composeapp.generated.resources.ic_social_tiktok_24dp
import lessmobile.composeapp.generated.resources.ic_social_whatsapp_24dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Item ids emitted by [ContactSupportBottomSheet]. Hosts switch on them to
 * open the matching channel (deep link / mailto / tel / web). Kept as a
 * sealed enum so call sites get exhaustive `when` checks rather than stringly
 * matching against magic ids.
 */
enum class ContactSupportChannel(val itemId: String) {
    Instagram("instagram"),
    TikTok("tiktok"),
    Facebook("facebook"),
    Telegram("telegram"),
    Whatsapp("whatsapp");

    companion object {
        fun fromId(id: String): ContactSupportChannel? =
            entries.firstOrNull { it.itemId == id }
    }
}

/**
 * Bottom sheet shown when the user taps "Contact with support" on the
 * previous-order info sheet. Built on top of [DsListBottomSheet] — same row
 * layout / icons / chevron as the More-screen "Contact us" sheet, but with
 * its own title key so the two flows can diverge later (e.g. the support
 * sheet may add a "Mail" or "Phone" channel without affecting Contact Us).
 *
 * The host owns the actual link launching to keep this composable
 * platform-agnostic and free of `expect`/`actual` plumbing.
 */
@Composable
fun ContactSupportBottomSheet(
    isVisible: Boolean,
    onChannelClick: (ContactSupportChannel) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    val items = listOf(
        ListBottomSheetItem(
            id = ContactSupportChannel.Instagram.itemId,
            title = stringResource(Res.string.contact_instagram),
            icon = painterResource(Res.drawable.ic_social_instagram_24dp)
        ),
        ListBottomSheetItem(
            id = ContactSupportChannel.TikTok.itemId,
            title = stringResource(Res.string.contact_tiktok),
            icon = painterResource(Res.drawable.ic_social_tiktok_24dp)
        ),
        ListBottomSheetItem(
            id = ContactSupportChannel.Facebook.itemId,
            title = stringResource(Res.string.contact_facebook),
            icon = painterResource(Res.drawable.ic_social_facebook_24dp)
        ),
        ListBottomSheetItem(
            id = ContactSupportChannel.Telegram.itemId,
            title = stringResource(Res.string.contact_telegram),
            icon = painterResource(Res.drawable.ic_social_telegram_24dp)
        ),
        ListBottomSheetItem(
            id = ContactSupportChannel.Whatsapp.itemId,
            title = stringResource(Res.string.contact_whatsapp),
            icon = painterResource(Res.drawable.ic_social_whatsapp_24dp)
        )
    )

    DsListBottomSheet(
        title = stringResource(Res.string.contact_support_title),
        items = items,
        onItemClick = { id ->
            ContactSupportChannel.fromId(id)?.let(onChannelClick)
        },
        onDismiss = onDismiss,
        chevronIcon = painterResource(Res.drawable.ic_chevron_right_24dp)
    )
}
