package az.less.mobile.domain.model

import az.less.mobile.data.remote.model.FilterBoxDto
import az.less.mobile.data.remote.model.SearchByFilterBoxesDto
import az.less.mobile.data.remote.model.SearchByFilterVenuesDto
import az.less.mobile.data.remote.model.SearchVenueDto
import az.less.mobile.presentation.client.main.offers.models.OfferItem
import az.less.mobile.presentation.client.main.offers.models.OfferMerchant

data class SearchVenuesResult(
    val venues: List<SearchVenue> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 20
)

data class SearchBoxesResult(
    val boxes: List<FilterBox> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 20
)

data class SearchVenue(
    val id: String,
    val name: String,
    val logo: String? = null,
    val address: String? = null,
    val description: String? = null,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double? = null,
    val rating: Double = 0.0,
    val totalReviews: Int = 0,
    val activeBoxes: Int = 0,
    val itemsOnSale: Int = 0,
    val hasActiveOffers: Boolean = false,
    val badgeText: String? = null,
    val ratingAndDistance: String? = null
)

data class FilterBox(
    val id: String,
    val title: String,
    val description: String? = null,
    val images: List<String> = emptyList(),
    val imageUrl: String? = null,
    val imageBgColor: String = "#fff2eb",
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val currentPrice: Double? = null,
    val quantity: Int = 0,
    val availableItems: Int = 0,
    val bagType: String? = null,
    val category: String? = null,
    val pickupTime: String? = null,
    val venueName: String? = null,
    val venueLogoUrl: String? = null,
    val venueLatitude: Double = 0.0,
    val venueLongitude: Double = 0.0,
    val venueDistanceKm: Double? = null,
    val venueRating: Double = 0.0
)

fun FilterBox.toOfferItem() = OfferItem(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl ?: images.firstOrNull(),
    imageBgColor = imageBgColor,
    quantity = if (availableItems > 0) availableItems else quantity,
    originalPrice = originalPrice.toString(),
    currentPrice = (currentPrice ?: discountedPrice).toString(),
    bagType = bagType,
    category = category ?: "",
    pickupTime = pickupTime ?: "",
    merchant = OfferMerchant(
        id = "",
        name = venueName ?: "",
        logoUrl = venueLogoUrl,
        latitude = venueLatitude,
        longitude = venueLongitude,
        rating = venueRating.toFloat()
    )
)

fun SearchByFilterVenuesDto.toDomain() = SearchVenuesResult(
    venues = venues.map { it.toDomain() },
    total = total,
    page = pagination?.page ?: 1,
    limit = pagination?.limit ?: 20
)

fun SearchByFilterBoxesDto.toDomain() = SearchBoxesResult(
    boxes = boxes.map { it.toDomain() },
    total = total,
    page = pagination?.page ?: 1,
    limit = pagination?.limit ?: 20
)

fun FilterBoxDto.toDomain() = FilterBox(
    id = id,
    title = title,
    description = description,
    images = images,
    imageUrl = imageUrl,
    imageBgColor = imageBgColor ?: "#fff2eb",
    originalPrice = originalPrice,
    discountedPrice = discountedPrice,
    currentPrice = currentPrice,
    quantity = quantity,
    availableItems = availableItems,
    bagType = bagType,
    category = category?.title,
    pickupTime = pickupTimeFormatted ?: pickupTime,
    venueName = venue?.name,
    venueLogoUrl = venue?.logo,
    venueLatitude = venue?.location?.coordinates?.getOrElse(1) { 0.0 } ?: 0.0,
    venueLongitude = venue?.location?.coordinates?.getOrElse(0) { 0.0 } ?: 0.0,
    venueDistanceKm = venue?.distanceKm,
    venueRating = 0.0
)

fun SearchVenueDto.toDomain() = SearchVenue(
    id = id,
    name = name,
    logo = logo,
    address = address,
    description = description,
    latitude = location.coordinates.getOrElse(1) { 0.0 },
    longitude = location.coordinates.getOrElse(0) { 0.0 },
    distanceKm = distanceKm,
    rating = rating,
    totalReviews = totalReviews,
    activeBoxes = activeBoxes,
    itemsOnSale = itemsOnSale,
    hasActiveOffers = hasActiveOffers,
    badgeText = badge?.text,
    ratingAndDistance = ratingAndDistance
)
