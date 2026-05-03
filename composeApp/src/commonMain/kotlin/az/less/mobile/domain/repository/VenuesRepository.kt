package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.AdminVenueDto
import az.less.mobile.data.remote.model.CreateBoxRequest
import az.less.mobile.data.remote.model.CreateBoxResponseDto
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
    ): NetworkResult<Unit>

    /**
     * Removes the merchant relationship from a venue. Takes the merchant relation id
     * (the venue-merchant `_id`), not the user id. The server auto-strips the
     * merchant role from the user if this was their only merchant relationship.
     */
    suspend fun removeVenueMerchant(
        merchantId: String
    ): NetworkResult<Unit>

    suspend fun createBox(request: CreateBoxRequest): NetworkResult<CreateBoxResponseDto>
}
