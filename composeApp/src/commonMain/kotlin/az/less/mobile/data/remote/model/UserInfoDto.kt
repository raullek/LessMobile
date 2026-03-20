package az.less.mobile.data.remote.model

import kotlinx.serialization.Serializable

/**
 * User profile response from API
 * GET /api/v1/users/profile
 */
@Serializable
data class UserProfileData(
    val user: UserProfileUser,
    val stats: UserProfileStats? = null,
    val ecoHeroBadge: UserProfileEcoBadge? = null
)

@Serializable
data class UserProfileUser(
    val id: String,
    val email: String,
    val name: String,
    val phone: String? = null,
    val avatar: String? = null,
    val gender: String? = null,
    val birthDay: String? = null,
    val roles: List<String> = emptyList(),
    val status: String = "",
    val emailVerified: Boolean = false,
    val authBlocked: Boolean = false,
    val authBlockedAt: String? = null,
    val authBlockedReason: String? = null,
    val passwordLoginEnabled: Boolean = false,
    val currentLocation: String? = null,
    val currentCoordinates: UserCoordinates? = null,
    val preferences: UserPreferences? = null,
    val venue: UserProfileVenue? = null,
    val lastLoginAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class UserProfileVenue(
    val id: String,
    val name: String,
    val businessName: String? = null,
    val businessAddress: String? = null,
    val businessDescription: String? = null,
    val businessLogo: String? = null,
    val coverImage: String? = null,
    val lotImage: String? = null,
    val defaultBoxTitle: String? = null,
    val defaultBoxDescription: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val location: UserCoordinates? = null,
    val createdBy: String? = null,
    val adminUserIds: List<String> = emptyList(),
    val status: String? = null,
    val rating: Double = 0.0,
    val totalReviews: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class UserCoordinates(
    val type: String = "Point",
    val coordinates: List<Double> = emptyList()
)

@Serializable
data class UserProfileStats(
    val co2Saved: Double = 0.0,
    val moneySaved: Double = 0.0,
    val mealsSaved: Int = 0
)

@Serializable
data class UserProfileEcoBadge(
    val level: String? = null,
    val message: String? = null,
    val mealsSaved: Int = 0,
    val icon: String? = null,
    val color: String? = null
)

@Serializable
data class UserPreferences(
    val notifications: Boolean = true,
    val darkMode: Boolean = false,
    val locale: String? = null
)
