package az.less.mobile.data.repository

import az.less.mobile.data.datasource.OffersDataSource
import az.less.mobile.data.remote.model.BoxDetailDto
import az.less.mobile.data.remote.model.BoxVenueDto
import az.less.mobile.data.remote.model.CategoryDto
import az.less.mobile.data.remote.model.OfferDto
import az.less.mobile.data.remote.model.VenueDto
import az.less.mobile.data.remote.model.OfferSectionDto
import az.less.mobile.data.remote.model.OffersScreenDto
import az.less.mobile.data.remote.model.SegmentedCategoryDto
import az.less.mobile.data.remote.model.SpecialCategoryDto
import az.less.mobile.domain.model.BoxDetail
import az.less.mobile.domain.model.BoxVenue
import az.less.mobile.domain.model.OffersHomeData
import az.less.mobile.domain.repository.OffersRepository
import az.less.mobile.network.NetworkResult
import az.less.mobile.presentation.client.main.offers.models.Category
import az.less.mobile.presentation.client.main.offers.models.OfferItem
import az.less.mobile.presentation.client.main.offers.models.OfferMerchant
import az.less.mobile.presentation.client.main.offers.models.OfferSection
import az.less.mobile.presentation.client.main.offers.models.SegmentedCategory
import az.less.mobile.presentation.client.main.offers.models.SpecialDiscountItem
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_explore_24dp
import lessmobile.composeapp.generated.resources.ic_mark_16dp
import lessmobile.composeapp.generated.resources.ic_star_16dp

class OffersRepositoryImpl(
    private val offersDataSource: OffersDataSource
) : OffersRepository {

    override suspend fun getHomeOffers(
        latitude: Double?,
        longitude: Double?,
        limit: Int
    ): NetworkResult<OffersHomeData> {
        return offersDataSource.getHomeOffers(latitude, longitude, limit)
            .map { it.toDomain() }
    }

    override suspend fun getBoxDetail(boxId: String): NetworkResult<BoxDetail> {
        return offersDataSource.getBoxDetail(boxId)
            .map { it.toDomain() }
    }
}

private fun OffersScreenDto.toDomain() = OffersHomeData(
    categories = categories.map { it.toDomain() },
    specialCategories = specialCategories.map { it.toDomain() },
    segmentedCategories = segmentedCategories.mapIndexed { index, dto -> dto.toDomain(index) },
    offerSections = offerSections.map { it.toDomain() }
)

private fun CategoryDto.toDomain() = Category(
    id = id,
    type = type,
    title = title,
    imageUrl = imageUrl
)

private fun SpecialCategoryDto.toDomain() = SpecialDiscountItem(
    id = id,
    type = type,
    title = title,
    description = description,
    imageUrl = imageUrl
)

private fun SegmentedCategoryDto.toDomain(index: Int): SegmentedCategory {
    val (icon, iconTint) = when (index) {
        0 -> Res.drawable.ic_explore_24dp to 0xFFFF8B38L
        1 -> Res.drawable.ic_star_16dp to 0xFF5AA9E7L
        2 -> Res.drawable.ic_mark_16dp to 0xFFAD3CDAL
        else -> null to null
    }
    return SegmentedCategory(
        id = id,
        type = type,
        title = title,
        icon = icon,
        iconTint = iconTint
    )
}

private fun OfferSectionDto.toDomain() = OfferSection(
    id = id,
    type = type,
    title = title,
    offers = offers.map { it.toDomain() }
)

private fun OfferDto.toDomain() = OfferItem(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    imageBgColor = imageBgColor ?: "#fff2eb",
    quantity = quantity,
    originalPrice = originalPrice.toString(),
    currentPrice = currentPrice.toString(),
    bagType = bagType,
    category = category,
    pickupTime = pickupTime,
    merchant = venue.toDomain()
)

private fun VenueDto.toDomain() = OfferMerchant(
    id = id,
    name = name,
    logoUrl = logoUrl,
    latitude = location.coordinates.getOrElse(1) { 0.0 },
    longitude = location.coordinates.getOrElse(0) { 0.0 },
    rating = rating.toFloat()
)

private fun BoxDetailDto.toDomain() = BoxDetail(
    id = id,
    title = title,
    description = description ?: "",
    originalPrice = originalPrice,
    discountedPrice = discountedPrice,
    availableQuantity = (quantity - soldCount).coerceAtLeast(0),
    status = status ?: "",
    boxType = boxType ?: "",
    category = category ?: "",
    pickupTimeFormatted = formatPickupTime(pickupTimeStart, pickupTimeEnd),
    images = images,
    dietaryInfo = dietaryInfo,
    tags = tags,
    venue = venue.toDomain(),
    address = address ?: "",
    collectionNotes = collectionNotes ?: ""
)

private fun BoxVenueDto.toDomain() = BoxVenue(
    id = id,
    name = name,
    businessName = businessName ?: name,
    businessLogo = businessLogo,
    businessAddress = businessAddress ?: "",
    phone = phone ?: ""
)

private fun formatPickupTime(start: String?, end: String?): String {
    if (start == null || end == null) return ""
    val startTime = extractTime(start)
    val endTime = extractTime(end)
    return "Pick up from $startTime to $endTime"
}

private fun extractTime(isoString: String): String {
    // Extract HH:mm from ISO datetime like "2026-02-22T17:00:00Z" or just "17:00"
    val timeIndex = isoString.indexOf('T')
    val timePart = if (timeIndex >= 0) isoString.substring(timeIndex + 1) else isoString
    return timePart.take(5) // "HH:mm"
}
