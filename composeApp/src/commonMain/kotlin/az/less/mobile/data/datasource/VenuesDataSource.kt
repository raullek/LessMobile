package az.less.mobile.data.datasource

import az.less.mobile.data.remote.model.AddVenueMerchantRequest
import az.less.mobile.data.remote.model.AdminVenueDto
import az.less.mobile.data.remote.model.CreateBoxRequest
import az.less.mobile.data.remote.model.CreateBoxResponseDto
import az.less.mobile.data.remote.model.VenueMerchantDto
import az.less.mobile.data.remote.model.VenuesDataDto
import az.less.mobile.network.NetworkResult
import az.less.mobile.network.safeApiCall
import az.less.mobile.network.safeApiCallUnit
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

class VenuesDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getAllVenues(
        page: Int = 1,
        limit: Int = 20
    ): NetworkResult<VenuesDataDto> {
        return safeApiCall {
            httpClient.get("v1/venues") {
                parameter("page", page)
                parameter("limit", limit)
            }
        }
    }

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
    ): NetworkResult<AdminVenueDto> {
        return safeApiCall {
            httpClient.submitFormWithBinaryData(
                url = "v1/venues",
                formData = buildVenueFormData(
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
            ) {
                method = HttpMethod.Post
            }
        }
    }

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
    ): NetworkResult<AdminVenueDto> {
        return safeApiCall {
            httpClient.submitFormWithBinaryData(
                url = "v1/venues/$id",
                formData = buildVenueFormData(
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
            ) {
                method = HttpMethod.Patch
            }
        }
    }

    suspend fun getVenueMerchants(venueId: String): NetworkResult<List<VenueMerchantDto>> {
        return safeApiCall {
            httpClient.get("v1/venues/$venueId/merchants")
        }
    }

    suspend fun addVenueMerchant(
        venueId: String,
        userIdentifier: String,
        name: String,
        email: String,
        phone: String
    ): NetworkResult<Unit> {
        return safeApiCallUnit {
            httpClient.post("v1/venues/$venueId/merchants") {
                setBody(
                    AddVenueMerchantRequest(
                        userIdentifier = userIdentifier,
                        name = name,
                        email = email,
                        phone = phone,
                        venueId = venueId
                    )
                )
            }
        }
    }

    /**
     * Removes the merchant relationship from a venue. The merchant loses access to
     * manage boxes/orders for this venue. The user account remains active. If this
     * was the user's only merchant relationship, the server auto-strips the merchant
     * role from the user.
     *
     * Path takes the merchant relation id (the venue-merchant `_id`), not the user id.
     */
    suspend fun removeVenueMerchant(
        merchantId: String
    ): NetworkResult<Unit> {
        return safeApiCallUnit {
            httpClient.delete("v1/venues/merchants/$merchantId")
        }
    }

    suspend fun createBox(request: CreateBoxRequest): NetworkResult<CreateBoxResponseDto> {
        return safeApiCall {
            httpClient.post("v1/boxes") {
                setBody(request)
            }
        }
    }

    private fun buildVenueFormData(
        name: String? = null,
        businessName: String? = null,
        businessAddress: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        businessDescription: String? = null,
        defaultBoxDescription: String? = null,
        phone: String? = null,
        email: String? = null,
        makeMeMerchant: Boolean? = null,
        status: String? = null,
        coverImage: ByteArray? = null,
        businessLogo: ByteArray? = null,
        lotImage: ByteArray? = null
    ) = formData {
        name?.let { append("name", it) }
        businessName?.let { append("businessName", it) }
        businessAddress?.let { append("businessAddress", it) }
        if (latitude != null && longitude != null) {
            append("coordinates[latitude]", latitude.toString())
            append("coordinates[longitude]", longitude.toString())
        }
        businessDescription?.let { append("businessDescription", it) }
        defaultBoxDescription?.let { append("defaultBoxDescription", it) }
        phone?.let { append("phone", it) }
        email?.let { append("email", it) }
        makeMeMerchant?.let { append("makeMeMerchant", it.toString()) }
        status?.let { append("status", it) }

        coverImage?.let {
            append("coverImage", it, Headers.build {
                append(HttpHeaders.ContentType, "image/jpeg")
                append(HttpHeaders.ContentDisposition, "filename=\"cover.jpg\"")
            })
        }
        businessLogo?.let {
            append("businessLogo", it, Headers.build {
                append(HttpHeaders.ContentType, "image/jpeg")
                append(HttpHeaders.ContentDisposition, "filename=\"logo.jpg\"")
            })
        }
        lotImage?.let {
            append("lotImage", it, Headers.build {
                append(HttpHeaders.ContentType, "image/jpeg")
                append(HttpHeaders.ContentDisposition, "filename=\"lot.jpg\"")
            })
        }
    }
}
