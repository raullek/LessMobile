package az.less.mobile.data.repository

import az.less.mobile.data.datasource.VenuesDataSource
import az.less.mobile.data.remote.model.AddVenueMerchantResponseDto
import az.less.mobile.data.remote.model.AdminVenueDto
import az.less.mobile.data.remote.model.VenueMerchantDto
import az.less.mobile.data.remote.model.VenuesDataDto
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.network.NetworkResult

class VenuesRepositoryImpl(
    private val venuesDataSource: VenuesDataSource
) : VenuesRepository {

    override suspend fun getAllVenues(page: Int, limit: Int): NetworkResult<VenuesDataDto> {
        return venuesDataSource.getAllVenues(page, limit)
    }

    override suspend fun createVenue(
        name: String,
        businessName: String?,
        businessAddress: String?,
        latitude: Double?,
        longitude: Double?,
        businessDescription: String?,
        defaultBoxDescription: String?,
        phone: String?,
        email: String?,
        makeMeMerchant: Boolean,
        coverImage: ByteArray?,
        businessLogo: ByteArray?,
        lotImage: ByteArray?
    ): NetworkResult<AdminVenueDto> {
        return venuesDataSource.createVenue(
            name = name,
            businessName = businessName,
            businessAddress = businessAddress,
            latitude = latitude,
            longitude = longitude,
            businessDescription = businessDescription,
            defaultBoxDescription = defaultBoxDescription,
            phone = phone,
            email = email,
            makeMeMerchant = makeMeMerchant,
            coverImage = coverImage,
            businessLogo = businessLogo,
            lotImage = lotImage
        )
    }

    override suspend fun updateVenue(
        id: String,
        name: String?,
        businessName: String?,
        businessAddress: String?,
        latitude: Double?,
        longitude: Double?,
        businessDescription: String?,
        defaultBoxDescription: String?,
        phone: String?,
        email: String?,
        status: String?,
        coverImage: ByteArray?,
        businessLogo: ByteArray?,
        lotImage: ByteArray?
    ): NetworkResult<AdminVenueDto> {
        return venuesDataSource.updateVenue(
            id = id,
            name = name,
            businessName = businessName,
            businessAddress = businessAddress,
            latitude = latitude,
            longitude = longitude,
            businessDescription = businessDescription,
            defaultBoxDescription = defaultBoxDescription,
            phone = phone,
            email = email,
            status = status,
            coverImage = coverImage,
            businessLogo = businessLogo,
            lotImage = lotImage
        )
    }

    override suspend fun getVenueMerchants(venueId: String): NetworkResult<List<VenueMerchantDto>> {
        return venuesDataSource.getVenueMerchants(venueId)
    }

    override suspend fun addVenueMerchant(
        venueId: String,
        userIdentifier: String,
        name: String,
        email: String,
        phone: String
    ): NetworkResult<AddVenueMerchantResponseDto> {
        return venuesDataSource.addVenueMerchant(
            venueId = venueId,
            userIdentifier = userIdentifier,
            name = name,
            email = email,
            phone = phone
        )
    }
}
