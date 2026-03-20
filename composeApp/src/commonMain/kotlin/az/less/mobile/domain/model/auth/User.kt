package az.less.mobile.domain.model.auth

data class User(
    val id: String,
    val name: String,
    val email: String,
    val roles: List<String> = emptyList(),
    val status: String = "",
    val avatarUrl: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val birthDay: String? = null,
    val emailVerified: Boolean = false,
    val currentLocation: String? = null,
    val venue: UserVenue? = null,
    val stats: UserStats? = null,
    val ecoHeroBadge: UserEcoHeroBadge? = null
) {
    val isPartner: Boolean get() = roles.contains("partner")
    val isMerchant: Boolean get() = roles.contains("merchant")
    val isClient: Boolean get() = roles.contains("client")

    fun availableModes(): Set<AppMode> {
        val modes = mutableSetOf<AppMode>()
        if (isClient) modes.add(AppMode.CLIENT)
        if (isMerchant || isPartner) modes.add(AppMode.MERCHANT)
        return modes
    }

    fun canSwitchMode(): Boolean = isClient && (isMerchant || isPartner)
}

data class UserVenue(
    val id: String,
    val name: String,
    val businessName: String? = null,
    val businessAddress: String? = null,
    val businessDescription: String? = null,
    val businessLogo: String? = null,
    val coverImage: String? = null,
    val rating: Double? = null,
    val totalReviews: Int? = null,
    val status: String? = null
)

data class UserStats(
    val mealsSaved: Int = 0,
    val co2Saved: Double = 0.0,
    val moneySaved: Double = 0.0
)

data class UserEcoHeroBadge(
    val level: String? = null,
    val message: String? = null,
    val mealsSaved: Int = 0,
    val icon: String? = null,
    val color: String? = null
)
