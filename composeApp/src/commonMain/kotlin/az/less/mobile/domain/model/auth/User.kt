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
    val ecoHeroBadge: UserEcoHeroBadge? = null,
    val appDefaults: AppDefaults? = null
) {
    // Atomic roles — server source of truth.
    val isPartner: Boolean get() = roles.contains("partner")
    val isMerchant: Boolean get() = roles.contains("merchant")
    val isClient: Boolean get() = roles.contains("client")

    // Semantic flags — derived from atomic roles. Use these in feature code instead of
    // referring to atomic roles directly, so intent is self-documenting.
    val usesMerchantFlow: Boolean get() = isMerchant
    val usesPartnerFlow: Boolean get() = isPartner && !isMerchant
    val canManageBranches: Boolean get() = isMerchant && isPartner
    val hasSingleLinkedVenue: Boolean get() = isMerchant && !isPartner

    val userKind: UserKind get() = when {
        isClient && isMerchant && isPartner -> UserKind.ClientMerchantPartner
        isClient && isMerchant              -> UserKind.ClientMerchant
        isClient && isPartner               -> UserKind.ClientPartner
        isClient                            -> UserKind.Client
        else                                -> UserKind.Other
    }

    fun availableModes(): Set<AppMode> {
        val modes = mutableSetOf<AppMode>()
        if (isClient) modes.add(AppMode.CLIENT)
        if (usesMerchantFlow) modes.add(AppMode.MERCHANT)
        if (usesPartnerFlow) modes.add(AppMode.PARTNER)
        return modes
    }

    fun canSwitchMode(): Boolean = isClient && (isMerchant || isPartner)

    /**
     * Picks the navigation root after login or app start.
     * Preserves [lastMode] if it is still available, otherwise falls back to [AppMode.CLIENT].
     */
    fun resolveStartMode(lastMode: AppMode): AppMode =
        if (lastMode in availableModes()) lastMode else AppMode.CLIENT

    /**
     * Picks the non-client mode the user can switch into from the client flow.
     * Returns null for pure client users.
     */
    fun nonClientMode(): AppMode? = availableModes().firstOrNull { it != AppMode.CLIENT }
}

enum class UserKind {
    Client,
    ClientMerchant,
    ClientPartner,
    ClientMerchantPartner,
    Other
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

data class AppDefaults(
    val serviceeFeeRate: Double = 0.0
)
