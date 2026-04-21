package az.less.mobile.data.remote.model.mapper

import az.less.mobile.data.remote.model.UserProfileData
import az.less.mobile.domain.model.auth.AppDefaults
import az.less.mobile.domain.model.auth.User
import az.less.mobile.domain.model.auth.UserEcoHeroBadge
import az.less.mobile.domain.model.auth.UserStats
import az.less.mobile.domain.model.auth.UserVenue

fun UserProfileData.toUser(): User = User(
    id = user.id,
    name = user.name,
    email = user.email,
    roles = user.roles,
    status = user.status,
    avatarUrl = user.avatar,
    phone = user.phone,
    gender = user.gender,
    birthDay = user.birthDay,
    emailVerified = user.emailVerified,
    currentLocation = user.currentLocation,
    venue = user.venue?.let {
        UserVenue(
            id = it.id,
            name = it.name,
            businessName = it.businessName,
            businessAddress = it.businessAddress,
            businessDescription = it.businessDescription,
            businessLogo = it.businessLogo,
            coverImage = it.coverImage,
            rating = it.rating,
            totalReviews = it.totalReviews,
            status = it.status
        )
    },
    stats = stats?.let {
        UserStats(
            mealsSaved = it.mealsSaved,
            co2Saved = it.co2Saved,
            moneySaved = it.moneySaved
        )
    },
    ecoHeroBadge = ecoHeroBadge?.let {
        UserEcoHeroBadge(
            level = it.level,
            message = it.message,
            mealsSaved = it.mealsSaved,
            icon = it.icon,
            color = it.color
        )
    },
    appDefaults = appDefaults?.let {
        AppDefaults(
            serviceeFeeRate = it.serviceFee?.rate ?: 0.0
        )
    }
)
