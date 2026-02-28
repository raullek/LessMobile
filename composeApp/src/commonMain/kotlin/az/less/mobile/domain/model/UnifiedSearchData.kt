package az.less.mobile.domain.model

import az.less.mobile.data.remote.model.SearchBoxDto
import az.less.mobile.data.remote.model.SearchVenueDto
import az.less.mobile.data.remote.model.UnifiedSearchDto

data class UnifiedSearchData(
    val venues: List<SearchVenue> = emptyList(),
    val boxes: List<SearchBox> = emptyList(),
    val total: Int = 0
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

data class SearchBox(
    val id: String,
    val title: String,
    val description: String? = null,
    val images: List<String> = emptyList(),
    val originalPrice: Double = 0.0,
    val discountedPrice: Double = 0.0,
    val availableItems: Int = 0,
    val venueId: String,
    val venueName: String,
    val venueLogo: String? = null,
    val venueAddress: String? = null,
    val venueLatitude: Double = 0.0,
    val venueLongitude: Double = 0.0,
    val venueDistanceKm: Double? = null
)

fun UnifiedSearchDto.toDomain() = UnifiedSearchData(
    venues = venues.map { it.toDomain() },
    boxes = boxes.map { it.toDomain() },
    total = total
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

fun SearchBoxDto.toDomain() = SearchBox(
    id = id,
    title = title,
    description = description,
    images = images,
    originalPrice = originalPrice,
    discountedPrice = discountedPrice,
    availableItems = availableItems,
    venueId = venue.id,
    venueName = venue.name,
    venueLogo = venue.logo,
    venueAddress = venue.address,
    venueLatitude = venue.location.coordinates.getOrElse(1) { 0.0 },
    venueLongitude = venue.location.coordinates.getOrElse(0) { 0.0 },
    venueDistanceKm = venue.distanceKm
)
