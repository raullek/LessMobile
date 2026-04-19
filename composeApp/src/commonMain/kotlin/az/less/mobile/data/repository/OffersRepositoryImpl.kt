package az.less.mobile.data.repository

import az.less.mobile.data.datasource.OffersDataSource
import az.less.mobile.data.remote.model.BoxDetailDto
import az.less.mobile.data.remote.model.BoxVenueDto
import az.less.mobile.data.remote.model.DefaultPaymentDto
import az.less.mobile.data.remote.model.PaymentCardDto
import az.less.mobile.data.remote.model.PlaceOrderData
import az.less.mobile.data.remote.model.RegisterCardDto
import az.less.mobile.data.remote.model.CategoryDto
import az.less.mobile.data.remote.model.HomepageButtonDto
import az.less.mobile.data.remote.model.OfferDto
import az.less.mobile.data.remote.model.VenueDto
import az.less.mobile.data.remote.model.OffersScreenDto
import az.less.mobile.data.remote.model.SpecialCategoryDto
import az.less.mobile.data.remote.model.SpecialSegmentDto
import az.less.mobile.domain.model.BoxDetail
import az.less.mobile.domain.model.BoxVenue
import az.less.mobile.domain.model.CardBrand
import az.less.mobile.domain.model.OffersHomeData
import az.less.mobile.domain.model.PaymentMethod
import az.less.mobile.utils.extractTime
import az.less.mobile.domain.repository.OffersRepository
import az.less.mobile.network.NetworkResult
import az.less.mobile.presentation.client.main.offers.models.Category
import az.less.mobile.presentation.client.main.offers.models.CategoryFilter
import az.less.mobile.presentation.client.main.offers.models.HomepageButton
import az.less.mobile.presentation.client.main.offers.models.OfferItem
import az.less.mobile.presentation.client.main.offers.models.OfferMerchant
import az.less.mobile.presentation.client.main.offers.models.OfferSection
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

    override suspend fun getDefaultPayment(): NetworkResult<PaymentMethod> {
        return offersDataSource.getDefaultPayment().map { it.toDomain() }
    }

    override suspend fun getPaymentMethods(): NetworkResult<List<PaymentMethod>> {
        return offersDataSource.getPaymentMethods().map { dto ->
            dto.cards.map { it.toDomain() }
        }
    }

    override suspend fun setDefaultPaymentMethod(methodId: String): NetworkResult<PaymentMethod> {
        return offersDataSource.setDefaultPaymentMethod(methodId).map { it.toDomain() }
    }

    override suspend fun placeOrder(
        boxId: String,
        quantity: Int,
        paymentMethodId: String,
        notes: String?,
        userVoucherId: String?
    ): NetworkResult<PlaceOrderData> {
        return offersDataSource.placeOrder(boxId, quantity, paymentMethodId, notes, userVoucherId)
    }

    override suspend fun registerCard(): NetworkResult<RegisterCardDto> {
        return offersDataSource.registerCard()
    }

    override suspend fun verifyCard(callbackUrl: String): NetworkResult<Boolean> {
        return offersDataSource.verifyCard(callbackUrl)
    }
}

private fun OffersScreenDto.toDomain() = OffersHomeData(
    categories = categories.map { it.toDomain() },
    specialCategories = specialCategories.map { it.toDomain() },
    specialSegments = specialSegments.map { it.toDomain() },
    homepageButtons = homepageButtons.mapIndexed { index, dto -> dto.toDomain(index) }
)

private fun CategoryDto.toDomain() = Category(
    id = id,
    type = type.orEmpty(),
    title = title.orEmpty(),
    imageUrl = imageUrl,
    filters = filters.map { CategoryFilter(searchFilterId = it.searchFilterId, values = it.values) },
    searchUrl = searchUrl
)

private fun SpecialCategoryDto.toDomain() = SpecialDiscountItem(
    id = id,
    type = type.orEmpty(),
    title = title.orEmpty(),
    description = description.orEmpty(),
    imageUrl = imageUrl,
    filters = filters.map { CategoryFilter(searchFilterId = it.searchFilterId, values = it.values) }
)

private fun SpecialSegmentDto.toDomain() = OfferSection(
    id = id,
    title = title.orEmpty(),
    offers = boxes.map { it.toDomain() },
    searchUrl = searchUrl,
    filters = filters.map { CategoryFilter(searchFilterId = it.searchFilterId, values = it.values) }
)

private fun HomepageButtonDto.toDomain(index: Int): HomepageButton {
    val (icon, iconTint) = when (index) {
        0 -> Res.drawable.ic_explore_24dp to 0xFFFF8B38L
        1 -> Res.drawable.ic_star_16dp to 0xFF5AA9E7L
        2 -> Res.drawable.ic_mark_16dp to 0xFFAD3CDAL
        else -> null to null
    }
    return HomepageButton(
        id = id,
        type = type.orEmpty(),
        title = title.orEmpty(),
        searchUrl = searchUrl,
        icon = icon,
        iconTint = iconTint
    )
}

private fun OfferDto.toDomain() = OfferItem(
    id = id,
    title = title ?: defaultBoxTitle.orEmpty(),
    description = description ?: defaultBoxDescription,
    imageUrl = imageUrl,
    imageBgColor = imageBgColor ?: "#fff2eb",
    quantity = quantity,
    originalPrice = originalPrice.toString(),
    currentPrice = currentPrice.toString(),
    bagType = bagType,
    category = category.orEmpty(),
    pickupTime = pickupTime.orEmpty(),
    merchant = venue?.toDomain() ?: OfferMerchant(id = "", name = "", rating = 0f)
)

private fun VenueDto.toDomain() = OfferMerchant(
    id = id,
    name = name.orEmpty(),
    logoUrl = logoUrl,
    latitude = location?.coordinates?.getOrElse(1) { 0.0 } ?: 0.0,
    longitude = location?.coordinates?.getOrElse(0) { 0.0 } ?: 0.0,
    rating = rating.toFloat()
)

private fun BoxDetailDto.toDomain() = BoxDetail(
    id = id,
    title = title ?: defaultBoxTitle.orEmpty(),
    description = description ?: defaultBoxDescription ?: "",
    originalPrice = originalPrice,
    discountedPrice = discountedPrice,
    availableQuantity = (quantity - soldCount).coerceAtLeast(0),
    status = status ?: "",
    boxType = boxType ?: "",
    category = category?.title ?: "",
    pickupTimeFormatted = pickupTimeFormatted ?: formatPickupTime(pickupTimeStart, pickupTimeEnd),
    images = images,
    dietaryInfo = dietaryInfo,
    tags = tags.mapNotNull { it.title ?: it.value },
    venue = venue?.toDomain() ?: BoxVenue(
        id = "",
        name = "",
        businessName = "",
        businessLogo = null,
        businessAddress = "",
        phone = ""
    ),
    address = address ?: "",
    collectionNotes = collectionNotes ?: ""
)

private fun BoxVenueDto.toDomain() = BoxVenue(
    id = id,
    name = name.orEmpty(),
    businessName = businessName.orEmpty(),
    businessLogo = businessLogo,
    businessAddress = businessAddress ?: "",
    phone = phone ?: ""
)

private fun formatPickupTime(start: String?, end: String?): String {
    if (start == null || end == null) return ""
    val startTime = start.extractTime() ?: return ""
    val endTime = end.extractTime() ?: return ""
    return "Pick up from $startTime to $endTime"
}

private fun String?.toCardBrand(): CardBrand = when (this?.lowercase()) {
    "visa" -> CardBrand.VISA
    "mastercard" -> CardBrand.MASTERCARD
    else -> CardBrand.UNKNOWN
}

private fun DefaultPaymentDto.toDomain() = PaymentMethod(
    id = id,
    brand = brand.toCardBrand(),
    lastFourDigits = last4 ?: cardMask ?: "",
    displayName = displayName ?: "",
    isDefault = isDefault,
    message = message
)

private fun PaymentCardDto.toDomain() = PaymentMethod(
    id = id,
    brand = brand.toCardBrand(),
    lastFourDigits = last4 ?: cardMask ?: "",
    displayName = displayName ?: "",
    isDefault = isDefault
)
