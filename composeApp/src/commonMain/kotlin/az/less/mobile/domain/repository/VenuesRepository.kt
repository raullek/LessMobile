package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.AddVenueMerchantResponseDto
import az.less.mobile.data.remote.model.AdminVenueDto
import az.less.mobile.data.remote.model.VenueMerchantDto
import az.less.mobile.data.remote.model.VenuesDataDto
import az.less.mobile.network.NetworkResult

interface VenuesRepository {
    suspend fun getAllVenues(page: Int = 1, limit: Int = 20): NetworkResult<VenuesDataDto>

    suspend fun createVenue(
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
    ): NetworkResult<AdminVenueDto>

    suspend fun updateVenue(
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
    ): NetworkResult<AdminVenueDto>

    suspend fun getVenueMerchants(venueId: String): NetworkResult<List<VenueMerchantDto>>

    suspend fun addVenueMerchant(
        venueId: String,
        userIdentifier: String,
        name: String,
        email: String,
        phone: String
    ): NetworkResult<AddVenueMerchantResponseDto>
}
