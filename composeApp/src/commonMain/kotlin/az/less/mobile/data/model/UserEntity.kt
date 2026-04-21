package az.less.mobile.data.model

import az.less.mobile.domain.model.auth.AppDefaults
import az.less.mobile.domain.model.auth.User
import az.less.mobile.domain.model.auth.UserEcoHeroBadge
import az.less.mobile.domain.model.auth.UserStats
import az.less.mobile.domain.model.auth.UserVenue
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val roles: List<String>,
    val status: String,
    val avatarUrl: String? = null,
    val phone: String? = null,
    val gender: String? = null,
    val birthDay: String? = null,
    val emailVerified: Boolean = false,
    val currentLocation: String? = null,
    val venue: UserVenueEntity? = null,
    val stats: UserStatsEntity? = null,
    val ecoHeroBadge: UserEcoHeroBadgeEntity? = null,
    val appDefaults: AppDefaultsEntity? = null
) {
    fun toUser(): User = User(
        id = id,
        name = name,
        email = email,
        roles = roles,
        status = status,
        avatarUrl = avatarUrl,
        phone = phone,
        gender = gender,
        birthDay = birthDay,
        emailVerified = emailVerified,
        currentLocation = currentLocation,
        venue = venue?.toUserVenue(),
        stats = stats?.toUserStats(),
        ecoHeroBadge = ecoHeroBadge?.toUserEcoHeroBadge(),
        appDefaults = appDefaults?.toAppDefaults()
    )
}

@Serializable
data class UserVenueEntity(
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
) {
    fun toUserVenue(): UserVenue = UserVenue(
        id = id,
        name = name,
        businessName = businessName,
        businessAddress = businessAddress,
        businessDescription = businessDescription,
        businessLogo = businessLogo,
        coverImage = coverImage,
        rating = rating,
        totalReviews = totalReviews,
        status = status
    )
}

@Serializable
data class UserStatsEntity(
    val mealsSaved: Int = 0,
    val co2Saved: Double = 0.0,
    val moneySaved: Double = 0.0
) {
    fun toUserStats(): UserStats = UserStats(
        mealsSaved = mealsSaved,
        co2Saved = co2Saved,
        moneySaved = moneySaved
    )
}

@Serializable
data class UserEcoHeroBadgeEntity(
    val level: String? = null,
    val message: String? = null,
    val mealsSaved: Int = 0,
    val icon: String? = null,
    val color: String? = null
) {
    fun toUserEcoHeroBadge(): UserEcoHeroBadge = UserEcoHeroBadge(
        level = level,
        message = message,
        mealsSaved = mealsSaved,
        icon = icon,
        color = color
    )
}

@Serializable
data class AppDefaultsEntity(
    val serviceFeeRate: Double = 0.0
) {
    fun toAppDefaults(): AppDefaults = AppDefaults(
        serviceeFeeRate = serviceFeeRate
    )
}
