package az.less.mobile.domain.model

import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.box_type_small_bag
import lessmobile.composeapp.generated.resources.box_type_medium_bag
import lessmobile.composeapp.generated.resources.box_type_large_bag
import org.jetbrains.compose.resources.StringResource

enum class BoxType(val apiValue: String, val labelRes: StringResource) {
    SMALL("small_bag", Res.string.box_type_small_bag),
    MEDIUM("medium_bag", Res.string.box_type_medium_bag),
    LARGE("large_bag", Res.string.box_type_large_bag);

    companion object {
        fun fromApi(value: String?): BoxType? =
            entries.firstOrNull { it.apiValue == value }
    }
}
