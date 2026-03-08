package az.less.mobile.presentation.client.main.explore.models

enum class FilterType(val displayName: String? = null) {
    LIKED,
    NEAREST("Nearest"),
    RATING("Rating"),
    MORE_DISCOUNT("More discount")
}

enum class QuickFilter(
    val displayName: String?,
    val queryParam: String,
    val iconType: FilterIconType
) {
    FILTER_BUTTON(displayName = null, queryParam = "", iconType = FilterIconType.FILTER),
    FAVORITE(displayName = null, queryParam = "isFavorite", iconType = FilterIconType.HEART),
    OPEN_NOW(displayName = "Open now", queryParam = "openNow", iconType = FilterIconType.NONE),
    NEAREST(displayName = "Nearest", queryParam = "nearest", iconType = FilterIconType.NONE),
    HOT_DEALS(displayName = "Hot deals", queryParam = "hotDeals", iconType = FilterIconType.NONE)
}

